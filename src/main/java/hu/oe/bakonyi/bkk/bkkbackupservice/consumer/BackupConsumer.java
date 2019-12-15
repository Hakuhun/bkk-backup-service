package hu.oe.bakonyi.bkk.bkkbackupservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.BackupRepository;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.*;
import hu.oe.bakonyi.bkk.bkkbackupservice.model.BKKBusinessDataV4;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Log4j2
public class BackupConsumer {

    @Autowired
    BackupRepository repository;

    @Autowired
    ObjectMapper mapper;

    @KafkaListener(id = "${spring.kafka.consumer.group-id}", idIsGroup = true, concurrency = "2", topics = "${spring.kafka.topics.refinedBkk}")
    public void consume(@Payload String payload, @Headers MessageHeaders messageHeaders) throws JsonProcessingException {
        BKKBusinessDataV4 businessDataV3 = mapper.readValue(payload, BKKBusinessDataV4.class);
        Time time = Time.builder().month(businessDataV3.getMonth()).hour(businessDataV3.getHour()).dayOfWeek(businessDataV3.getDayOfWeek()).build();
        MDBBkkBackupIndex index = MDBBkkBackupIndex.builder().routeId(businessDataV3.getRouteId()).time(time).build();
        MDBBkkBackup backup = null;

        Route route = Route.builder().routeId(businessDataV3.getRouteId()).stopId(businessDataV3.getStopId()).alert(businessDataV3.getAlert() == 1).build();
        Weather weather = Weather.builder().humidity(businessDataV3.getHumidity()).temperature(businessDataV3.getTemperature()).visibility(businessDataV3.getVisibility()).pressure(businessDataV3.getPressure()).rain(businessDataV3.getRain()).snow(businessDataV3.getSnow()).build();
        MDBBkkBackupData data = MDBBkkBackupData.builder().route(route).weather(weather).value(businessDataV3.getLabel()).build();
        if (repository.existsById(index)) {
            backup = repository.findById(index).get();
            int size = backup.getDatas().size();
            backup.getDatas().add(data);
            repository.save(backup);
            log.info("Adat frissítve: " + index + " " + size + "->" + backup.getDatas().size());
        } else {
            backup = MDBBkkBackup.builder().datas(Arrays.asList(data))._id(index).build();
            repository.insert(backup);
            log.info("Adat hozzáadva: " + index);
        }
    }

}
