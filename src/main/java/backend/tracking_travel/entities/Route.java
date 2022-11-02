package backend.tracking_travel.entities;

import backend.tracking_travel.gpxWriteRead.TrackPoint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.util.List;

@Entity
@Table(name = "ROUTES")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "Не меньше 2 знаков")
    private String title;
    private String description;

    @OneToMany
    private List<TrackPoint> trackPoints;
    //private Long heightPeak;
    //private Long distanceRoute;
    //private Duration durationRoute;

    @OneToOne
    private FileGPX fileGPX;

    @OneToMany (mappedBy = "route", orphanRemoval = true)
    private List<Photo> photos;

    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;
}
