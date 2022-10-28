package backend.tracking_travel.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.util.Set;

@Entity
@Table(name = "t_route")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, message = "Не меньше 5 знаков")
    private String title;

    private String linkToGPX;
    private String description;
    private Long heightPeak;
    private Long distanceRoute;
    private Duration durationRoute;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Photo> photos;
}
