package de.haw_hamburg.mensamatch.adapter.persistence;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.haw_hamburg.mensamatch.adapter.persistence.criteria.model.CriteriaDao;
import de.haw_hamburg.mensamatch.adapter.persistence.meal.model.MealDao;
import de.haw_hamburg.mensamatch.adapter.persistence.user.model.UserDao;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.jsr310.LocalDateCodec;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@Configuration
public class MongoConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Bean
    public MongoClient mongoClient() {
        final MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(String.format("mongodb://%s:%s/%s", host, port, "admin")))
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        CodecRegistries.fromCodecs(
                                new LocalDateCodec()
                        ),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build())
                ))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .credential(MongoCredential.createCredential(username, "admin", password.toCharArray()))
                .build();
        return MongoClients.create(clientSettings);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase("mensa");
    }

    @Bean
    public MongoCollection<MealDao> mealCollection(MongoDatabase database) {
        database.createCollection("meals");
        return database.getCollection("meals", MealDao.class);
    }

    @Bean
    public MongoCollection<UserDao> userCollection(MongoDatabase database) {
        database.createCollection("users");
        return database.getCollection("users", UserDao.class);
    }

    @Bean
    public MongoCollection<CriteriaDao> criteriaCollection(MongoDatabase database) {
        database.createCollection("criteria");
        return database.getCollection("criteria", CriteriaDao.class);
    }
}
