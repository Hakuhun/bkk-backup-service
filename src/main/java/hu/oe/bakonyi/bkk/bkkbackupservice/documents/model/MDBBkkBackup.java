package hu.oe.bakonyi.bkk.bkkbackupservice.documents.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "routes")
public class MDBBkkBackup{
    @Id
    private MDBBkkBackupIndex _id;
    private List<MDBBkkBackupData> datas;
}
