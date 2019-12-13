package hu.oe.bakonyi.bkk.bkkbackupservice.model;

import hu.oe.bakonyi.bkk.bkkbackupservice.service.ConditionBuilder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AnalyzerRequestData {
    Instant realTime;
    String route;
    java.util.List<ConditionBuilder> a;
    List<ConditionBuilder> b;
}
