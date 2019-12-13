package hu.oe.bakonyi.bkk.bkkbackupservice.model;

import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.Time;
import hu.oe.bakonyi.bkk.bkkbackupservice.service.ConditionBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QueryingCase {
    private Time from;
    private Time to;
    private String route;
    private List<ConditionBuilder> conditions;
}
