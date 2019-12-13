package hu.oe.bakonyi.bkk.bkkbackupservice.model;

import lombok.Data;

@Data
public class BKKBusinessDataV4 {
    private int month, dayOfWeek, hour, routeId, stopId;
    private double temperature, humidity, pressure, snow, rain,visibility;
    private byte alert;
    private double label;
}
