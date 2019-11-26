package hu.oe.bakonyi.bkk.bkkbackupservice.documents.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MDBBkkBackupIndex {
    private Time time;
    private double routeId;
}
