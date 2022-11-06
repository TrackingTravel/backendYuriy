package backend.tracking_travel.gpxWriteRead;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
public class TrackPoint {
    private Long id;
    private double latitude;
    private double longitude;
    private Double elevation;
    private Date time;

    public TrackPoint() {
    }

    public TrackPoint(double latitude, double longitude, Double elevation, Date time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.time = time;
    }

    public double distanceTo(TrackPoint point) {
        boolean r = true;
        double latDistance = Math.toRadians(point.latitude - this.latitude);
        double lonDistance = Math.toRadians(point.longitude - this.longitude);
        double a = Math.sin(latDistance / 2.0D) * Math.sin(latDistance / 2.0D) + Math.cos(Math.toRadians(this.latitude))
                * Math.cos(Math.toRadians(point.latitude)) * Math.sin(lonDistance / 2.0D) * Math.sin(lonDistance / 2.0D);
        double c = 2.0D * Math.atan2(Math.sqrt(a), Math.sqrt(1.0D - a));
        double distance = 6371.0D * c * 1000.0D;
        if (point.elevation != null && this.elevation != null) {
            double height = point.elevation - this.elevation;
            distance = Math.pow(distance, 2.0D) + Math.pow(height, 2.0D);
        }

        return Math.sqrt(distance);
    }
}

