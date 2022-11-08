package backend.tracking_travel.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "MAP_PHOTO")
@Data
public class MapPhoto extends Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String uri;
    private String type;
    private Long size;

    @OneToOne
    private TestRoute testRoute;

    public MapPhoto (){}


}
