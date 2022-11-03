package backend.tracking_travel.entities;

import javax.persistence.*;

@Entity
@Table(name = "PHOTO")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String uri;
    private String type;
    private Long size;

    public Photo (){}

    public Photo(String name, String uri, String type, Long size) {
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.size = size;
    }
}
