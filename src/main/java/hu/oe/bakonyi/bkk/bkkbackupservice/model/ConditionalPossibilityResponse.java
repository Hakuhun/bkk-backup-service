package hu.oe.bakonyi.bkk.bkkbackupservice.model;

import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.Weather;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ConditionalPossibilityResponse {

    ResponseValue responseValue;
    Instant now;
    String route, message;
    Weather weather;
    double possibility;

    public enum ResponseValue{
        OK_CALCULATED, OK_NOSUCHDATA
    }
}
