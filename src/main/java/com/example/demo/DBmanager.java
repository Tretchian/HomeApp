package com.example.demo;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DBmanager {
    DateTimeFormatter arsform = DateTimeFormatter.ofPattern("dd.MM.uu , HH:mm:ss");
    ServerApi serverApi;
    String db_link;

    MongoClientSettings settings;
    MongoClient client;
    public static final String DB_LINK = "mongodb+srv://admin:admin@cluster0.gqdvqf4.mongodb.net/";
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

    public void sendData(){
        MongoDatabase sensors = client.getDatabase("sensors");
        MongoCollection<Document> collection = sensors.getCollection("temperature");
        InsertOneResult result = collection.insertOne(new Document()
                .append("sensor_id","Home app")
                .append("data",(int)(Math.random()*20)+"°C")
                .append("timestamp",LocalDateTime.now().format(arsform)));
    }

}
//   String connectionString = "mongodb+srv://admin:admin@cluster0.gqdvqf4.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
