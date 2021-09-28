import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.List;

public class MongoDBApp {
    public static void main(String[] args) {
        try {

            MongoClient mongoClient = new MongoClient("localhost");

            List<String> databases = mongoClient.getDatabaseNames();

            for (String dbName : databases) {
                System.out.println("- Database: " + dbName);
            }
            DB db = mongoClient.getDB("NSUBD");
            DBCollection myCollection = db.getCollection("test");
            DBObject hw = new BasicDBObject("speech", "Hello, Wolrd!");
            myCollection.insert(hw);
            DBCursor cursor = myCollection.find();
            System.out.println(cursor.next());
            mongoClient.close();

        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }

    }
}
