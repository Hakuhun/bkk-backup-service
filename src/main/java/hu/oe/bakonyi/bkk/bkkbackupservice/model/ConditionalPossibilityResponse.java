package hu.oe.bakonyi.bkk.bkkbackupservice.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ConditionalPossibilityResponse {

    ResponseValue responseValue;
    Instant now;
    String route, message;
    double possibility;

    public enum ResponseValue{
        OK_CALCULATED, OK_NOSUCHDATA
    }
}
