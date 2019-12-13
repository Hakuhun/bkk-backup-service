package hu.oe.bakonyi.bkk.bkkbackupservice.model;

import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackupData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class P {
    List<MDBBkkBackupData> data;
    int fullEvent;
    int positiveCases;
    double possibility;
}
