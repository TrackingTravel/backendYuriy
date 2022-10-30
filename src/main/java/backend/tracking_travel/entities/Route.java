package backend.tracking_travel.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Duration;

@Entity
@Table(name = "t_route")
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
    private String linkToGPX;
    private Long heightPeak;
    private Long distanceRoute;
    //private Duration durationRoute;

    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;
}
