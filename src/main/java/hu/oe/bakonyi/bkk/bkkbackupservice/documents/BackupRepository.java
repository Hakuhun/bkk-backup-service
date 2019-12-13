package hu.oe.bakonyi.bkk.bkkbackupservice.documents;

import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackup;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackupData;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackupIndex;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.Time;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BackupRepository extends MongoRepository<MDBBkkBackup, MDBBkkBackupIndex> {

    List<MDBBkkBackupData> findAllBy_idTimeAndDatasRouteRouteIdAndDatasRouteStopId(Time time, String routeId);

}
