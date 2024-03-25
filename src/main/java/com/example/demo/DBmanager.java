package com.example.demo;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Sorts.descending;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class DBmanager {

    public static final String DB_LINK = "mongodb+srv://admin:admin@cluster0.gqdvqf4.mongodb.net/";
    public static final String SENSORS = "sensors";
    public static final String TEMP = "temperature";
    public static final String HUMIDITY = "humidity";
    public static final String TARGET_TEMP = "target_temperature";
    public static final String TARGET_HUMIDITY = "target_humidity";

    DateTimeFormatter arsform = DateTimeFormatter.ofPattern("dd.MM.uu , HH:mm:ss");

    ServerApi serverApi;
    String db_link;

    MongoClientSettings settings;
    MongoClient client;

    public DBmanager(String link){
        this.db_link = link;
        serverApi  = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(db_link))
                .serverApi(serverApi)
                .build();
        this.client =  MongoClients.create(settings);
    }

    public void Ping(){
        try {
            // Send a ping to confirm a successful connection
            MongoDatabase database = client.getDatabase("admin");
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = database.runCommand(command);
            System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
        } catch (MongoException me) {
            System.err.println(me);
        }
    }

    public void sendData(String database,String collection_name,int data){
        MongoDatabase sensors = client.getDatabase(database);
        MongoCollection<Document> collection = sensors.getCollection(collection_name);
        InsertOneResult result = collection.insertOne(new Document()
                .append("sensor_id","Home app")
                .append("data",data)
                .append("timestamp",LocalDateTime.now().format(arsform)));
    }
    public void sendData(String database,String collection_name,boolean data){
        MongoDatabase sensors = client.getDatabase(database);
        MongoCollection<Document> collection = sensors.getCollection(collection_name);
        InsertOneResult result = collection.insertOne(new Document()
                .append("sensor_id","Home app")
                .append("data",data)
                .append("timestamp",LocalDateTime.now().format(arsform)));
    }
    public void editData(String database,String collection_name,String key,boolean data){
        MongoDatabase sensors = client.getDatabase(database);
        MongoCollection<Document> collection = sensors.getCollection(collection_name);
       Document filter = new Document()
                .append("switch_id",0);
       Document update = new Document()
                .append("$set",new Document(key,data));
        UpdateResult result = collection.updateOne(filter,update);
    }
    public void editDataInt(String database,String collection_name,String key, int data){
        MongoDatabase sensors = client.getDatabase(database);
        MongoCollection<Document> collection = sensors.getCollection(collection_name);
        Document filter = new Document()
                .append("switch_id",0);
        Document update = new Document()
                .append("$set",new Document(key,data));
        UpdateResult result = collection.updateOne(filter,update);
    }
    public String getData(String database_name,String collection_name,String key){
        MongoDatabase database = client.getDatabase(database_name);
        MongoCollection<Document> db_coll = database.getCollection(collection_name);
        Document data_in =  db_coll.find().first();
        assert data_in != null;
        return data_in.getString(key);
    }
    public boolean getDataBool(String database_name,String collection_name,String key){
        MongoDatabase database = client.getDatabase(database_name);
        MongoCollection<Document> db_coll = database.getCollection(collection_name);
        Document data_in =  db_coll.find().first();
        assert data_in != null;
        return data_in.getBoolean(key);
    }
    public String getLatestData(String database_name,String collection_name,String key){
        MongoDatabase database = client.getDatabase(database_name);
        MongoCollection<Document> db_coll = database.getCollection(collection_name);
        Document data_in =  db_coll.aggregate(Arrays.asList(Aggregates.sort(descending("timestamp")))).first();
        assert data_in != null;
        return data_in.get(key).toString();
    }

    public int[] getLatestN(String database_name,String collection_name,String key,int n){
        int output[] = new int[n];
        int i=0;
        AggregateIterable<Document> list = client.getDatabase(database_name)
                .getCollection(collection_name)
                .aggregate(Arrays.asList(Aggregates.sort(descending("timestamp"))));
        for (Document doc: list) {
            if (i>n-1) return  output;
            output[i] = (int) doc.get(key);
           // System.out.println(doc.get(key)); логирование значений в консоль (для дебага)
            i++;

        }
        return null;
    }
}
//   String connectionString = "mongodb+srv://admin:admin@cluster0.gqdvqf4.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
