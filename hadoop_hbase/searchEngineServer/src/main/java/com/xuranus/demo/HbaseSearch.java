package com.xuranus.demo;

import com.google.gson.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class HbaseSearch {

    private static Configuration configuration;
    private static Connection connection;
    private static Admin admin;

    public HbaseSearch() {
        init();
        //create("school_tb","attr");
        //buildSchoolData();
    }

    public School getSchoolInfo(String schoolName) {
        try {
            HTable table=new HTable(configuration,"school_tb");
            Get g = new Get(Bytes.toBytes(schoolName));
            Result result=table.get(g);
            return new School(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLinksByKeyword(String key) {
        try {
            HTable table=new HTable(configuration,"keyword_map");
            Get g = new Get(Bytes.toBytes(key));
            Result result = table.get(g);
            System.out.println("key="+key);
            System.out.println("result="+result);
            System.out.println("return="+new String(result.getValue("cf".getBytes(),"data".getBytes())));
            return new String(result.getValue("cf".getBytes(),"data".getBytes()));
            //this.found_time = new String(result.getValue("attr".getBytes(),"found_time".getBytes()));;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void buildSchoolData() {
        String path = "/home/xuranus/Desktop/hadoop/homework/all.json";
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        String jsonStr = "";
        String lineStr;
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(path)));
            while((lineStr = in.readLine())!=null) {
                jsonStr = jsonStr.concat(lineStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();

        JsonArray schools = jsonObject.get("data").getAsJsonArray();
        for(JsonElement school:schools) {
            String chinese_name = school.getAsJsonObject().get("中文名").getAsString();
            String location = school.getAsJsonObject().get("所属地区").getAsString();
            String abbreviation = school.getAsJsonObject().get("简称").getAsString();
            String major_department = school.getAsJsonObject().get("主要院系").getAsString();
            String found_time = school.getAsJsonObject().get("创办时间").getAsString();
            String english_name = school.getAsJsonObject().get("英文名").getAsString();
            String famous_alumni = school.getAsJsonObject().get("知名校友").getAsString();

            try {
                put("school_tb", chinese_name, "attr", "chinese_name", chinese_name);
                put("school_tb", chinese_name, "attr", "location", location);
                put("school_tb", chinese_name, "attr", "abbreviation", abbreviation);
                put("school_tb", chinese_name, "attr", "major_department", major_department);
                put("school_tb", chinese_name, "attr", "found_time", found_time);
                put("school_tb", chinese_name, "attr", "english_name", english_name);
                put("school_tb", chinese_name, "attr", "famous_alumni", famous_alumni);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public static void init() {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "zookeeper001,zookeeper002,zookeeper003");
        //configuration.addResource("/home/xuranus/hdfs-site.xml");
        configuration.set("hbase.master", "172.16.173.136:16000");
        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void create(String tablename,String columnFamily) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(configuration);
        if (admin.tableExists(tablename)) {
            System.out.println("table Exists!");
            System.exit(0);
        }
        else{
            HTableDescriptor tableDesc = new HTableDescriptor(tablename);
            tableDesc.addFamily(new HColumnDescriptor(columnFamily));
            admin.createTable(tableDesc);
            System.out.println("create table success!");
        }
    }

    //添加一条数据，通过HTable Put为已经存在的表来添加数据
    public static void put(String tablename,String row, String columnFamily,String column,String data) throws Exception {
        HTable table = new HTable(configuration, tablename);
        Put p1=new Put(Bytes.toBytes(row));
        p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
        table.put(p1);
        System.out.println("put '"+row+"','"+columnFamily+":"+column+"','"+data+"'");
    }

    public static void get(String tablename,String row) throws Exception{
        HTable table=new HTable(configuration,tablename);
        Get g=new Get(Bytes.toBytes(row));
        Result result=table.get(g);
        System.out.println("Get: "+result);
    }

    //显示所有数据，通过HTable Scan来获取已有表的信息
    public static void scan(String tablename) throws Exception{
        HTable table = new HTable(configuration, tablename);
        Scan s = new Scan();
        ResultScanner rs = table.getScanner(s);
        for(Result r:rs){
            System.out.println("Scan: "+r);
        }
    }

    public static boolean delete(String tablename) throws Exception{
        HBaseAdmin admin=new HBaseAdmin(configuration);
        if(admin.tableExists(tablename)){
            try
            {
                admin.disableTable(tablename);
                admin.deleteTable(tablename);
            }catch(Exception ex){
                ex.printStackTrace();
                return false;
            }

        }
        return true;
    }
}

