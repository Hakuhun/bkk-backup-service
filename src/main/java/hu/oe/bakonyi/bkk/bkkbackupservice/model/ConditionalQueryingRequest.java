package hu.oe.bakonyi.bkk.bkkbackupservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConditionalQueryingRequest {
    QueryingCase commissionerEvent;//B
    QueryingCase questionableEvent;//A
}
