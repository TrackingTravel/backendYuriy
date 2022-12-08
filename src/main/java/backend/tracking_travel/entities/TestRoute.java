package backend.tracking_travel.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "TEST_ROUTES")
@Data
@AllArgsConstructor
public class TestRoute {
    public TestRoute() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "Не меньше 2 знаков")
    private String title;
    private String description;
    private String linkToMap;

    private String heightPeak;
    private String distanceRoute;
    private String durationRoute;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    @JoinColumn(name = "testRouteId")
    private List<MapPhoto> mapPhoto;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    @JoinColumn(name = "testRouteId")
    private List<Photo> photo;

    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;

}
