import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

public class Main {
    public static void main(String[] args) {
        try {
            Test3.start();
            //Test1.start("hdfs://172.16.173.136:9000/input","hdfs://172.16.173.136:9000/output");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
