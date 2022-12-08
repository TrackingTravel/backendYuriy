package backend.tracking_travel.gpxWriteRead;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrackSegment {

    private List<TrackPoint> points;

    public TrackSegment() {
        this.points = new ArrayList();
    }

    public TrackSegment(List<TrackPoint> points) {
        this.points = points;
    }

    public void addTrackPoint(TrackPoint point) {
        this.points.add(point);
    }

    public void removeTrackPoint(int index) {
        this.points.remove(index);
    }

    public void clearTrackPoints() {
        this.points.clear();
    }

    public List<TrackPoint> getPoints() {
        return this.points;
    }

}

