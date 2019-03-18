import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;

public class Test3 {
    public static Configuration conf;
    public static void put(String tablename,String row, String columnFamily,String column,String data) throws Exception {
        HTable table = new HTable(Test3.conf, tablename);
        Put p1=new Put(Bytes.toBytes(row));
        p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
        table.put(p1);
        System.out.println("put '"+row+"','"+columnFamily+":"+column+"','"+data+"'");
    }

    public static class TokenizerMapper extends TableMapper<Text, Text> {
        private Text word = new Text();
        private Text valueInfo = new Text(); //存储词频

        public void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
            List<Cell> cs = value.listCells();
            for (Cell cell : cs) {
                byte[] bt = cell.getValue();
                InputStream ip = new ByteArrayInputStream(bt);
                Reader read = new InputStreamReader(ip);
                IKSegmenter iks = new IKSegmenter(read, true);
                Lexeme t;

                while ((t = iks.next()) != null) {
                    String rowkey = new String(key.get());
                    word.set(t.getLexemeText().concat(":").concat(rowkey));
                    valueInfo.set("1");
                    context.write(word, valueInfo);
                }
            }
        }
    }

    public static class InvertedIndexCombiner extends Reducer<Text, Text, Text, Text>{
        private Text info = new Text();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (Text value : values) {
                sum += Integer.parseInt(value.toString() );
            }
            int splitIndex = key.toString().indexOf(":");
            info.set( key.toString().substring( splitIndex + 1) +":"+sum );
            key.set( key.toString().substring(0,splitIndex));
            context.write(key, info);
        }
    }


    public static class InvertedIndexReducer extends Reducer<Text, Text, Text, Text>{
        private Text result = new Text();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)  throws IOException, InterruptedException {
            //生成文档列表
            String fileList = new String();
            for (Text value : values) {
                fileList += value.toString()+";";
            }
            result.set(fileList);
            context.write(key, result);
            try{
                System.out.println("-------InvertedIndexReducer-------");
                System.out.println("key="+key.toString());
                System.out.println("value="+fileList);
                System.out.println("----------------------------------");
                put("keyword_map",key.toString(),"cf","data",fileList);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static void start() throws Exception {
        System.setProperty("HADOOP_USER_NAME", "root");
        System.setProperty("hadoop.home.dir","/home/xuranus/Desktop/hadoop/hadoop-2.7.6");
        conf = new Configuration();
        String tablename="school_tb";
        String columnFamily="attr";
        conf.set("hbase.zookeeper.quorum","zookeeper001,zookeeper002,zookeeper003");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.master", "172.16.173.136:16000");

        conf.addResource("/home/xuranus/hdfs-site.xml");

        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(Test3.class);

        Scan scan = new Scan();

        scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("chinese"));
        scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("location"));
        scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("abbreviation"));
        scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("major_department"));
        scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("found_time"));
        scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("english_name"));
        scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("famous_alumni"));


        TableMapReduceUtil.initTableMapperJob(tablename, scan, TokenizerMapper.class,Text.class,  Text.class, job);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setCombinerClass(InvertedIndexCombiner.class);
        job.setReducerClass(InvertedIndexReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileOutputFormat.setOutputPath(job, new Path("hdfs://172.16.173.136:9000/output"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}