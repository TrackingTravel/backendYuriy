package backend.tracking_travel.entities;

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

    public Route(Long id, String title, String description, Country country) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.country = country;
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

    @OneToOne(orphanRemoval = true, cascade=CascadeType.ALL)
    private FileGPX fileGPX;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    private List<Photo> photos;

    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;
}
