package backend.tracking_travel.entities;

import backend.tracking_travel.gpxWriteRead.Track;
import backend.tracking_travel.gpxWriteRead.TrackPoint;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "ROUTES")
@Data
@AllArgsConstructor
public class Route {
    public Route() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "Не меньше 2 знаков")
    private String title;
    private String description;

    //private Long heightPeak;
    //private Long distanceRoute;
    //private Duration durationRoute;

    @OneToOne(orphanRemoval = true)
    private FileGPX fileGPX;

    @OneToMany(mappedBy = "route", orphanRemoval = true)
    private List<Photo> photos;

    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;
}
