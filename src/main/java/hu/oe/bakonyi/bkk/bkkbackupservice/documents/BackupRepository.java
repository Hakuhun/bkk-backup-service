package hu.oe.bakonyi.bkk.bkkbackupservice.documents;

import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackup;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackupIndex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BackupRepository extends MongoRepository<MDBBkkBackup, MDBBkkBackupIndex> {
}
