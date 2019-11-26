package hu.oe.bakonyi.bkk.bkkbackupservice.model;

import lombok.Data;

@Data
public class BkkBusinessDataVKafka {
    private int month, dayOfWeek, hour, routeId, stopId;
    private double temperature, humidity, pressure, snow, rain,visibility;
    private byte alert;
    private double value;
}
