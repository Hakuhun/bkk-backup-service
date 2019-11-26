package hu.oe.bakonyi.bkk.bkkbackupservice.documents.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Builder
public class MDBBkkBackupData {
    @Indexed
    Route route;
    Weather weather;
    double value;
}
