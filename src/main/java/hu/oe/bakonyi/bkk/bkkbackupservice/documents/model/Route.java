package hu.oe.bakonyi.bkk.bkkbackupservice.documents.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Builder
public class Route {
    @Indexed
    private double routeId;
    @Indexed
    private double stopId;
    private boolean alert;
}
