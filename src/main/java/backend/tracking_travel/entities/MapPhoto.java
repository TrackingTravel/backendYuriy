package backend.tracking_travel.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "MAP_PHOTO")
@Data
public class MapPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String uri;
    private String type;
    private Long size;

    public MapPhoto (){}

    public MapPhoto(String name, String uri, String type, Long size) {
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.size = size;
    }
}
