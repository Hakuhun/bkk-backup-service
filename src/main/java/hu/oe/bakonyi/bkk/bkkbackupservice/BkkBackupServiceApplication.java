package hu.oe.bakonyi.bkk.bkkbackupservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableMongoRepositories
@SpringBootApplication
public class BkkBackupServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BkkBackupServiceApplication.class, args);
    }

}
