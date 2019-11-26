package hu.oe.bakonyi.bkk.bkkbackupservice.documents.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Weather {
    private double temperature, humidity, pressure, visibility, snow, rain;
}
