package backend.tracking_travel.gpxWriteRead;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LEAVES
 * @Version 1.0
 * @Date: October 08, 202, 37 seconds
 * @Description: Collection of each point [a track]
 */

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

    public void setPoints(List<TrackPoint> points) {
        this.points = points;
    }
}

