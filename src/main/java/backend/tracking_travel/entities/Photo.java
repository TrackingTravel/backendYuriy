package backend.tracking_travel.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "PHOTO")
@Data
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String uri;
    private String type;
    private Long size;

    @ManyToOne(fetch = FetchType.EAGER)
    private Route route;

    @ManyToOne(fetch = FetchType.EAGER)
    private TestRoute testRoute;

    public Photo (){}

    public Photo(String name, String uri, String type, Long size) {
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.size = size;
    }

}
