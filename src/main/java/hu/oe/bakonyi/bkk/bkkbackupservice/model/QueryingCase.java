package hu.oe.bakonyi.bkk.bkkbackupservice.model;

import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.Time;
import hu.oe.bakonyi.bkk.bkkbackupservice.service.ConditionBuilder;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class QueryingCase {
    Time time;
    String route;
    List<ConditionBuilder> conditions;
}
