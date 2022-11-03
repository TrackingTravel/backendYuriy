package backend.tracking_travel.entities;

import javax.persistence.*;

@Entity
@Table(name = "PHOTO")
public class Photo extends FileResponse {
    public Photo(String name, String uri, String type, Long size) {
        super(name, uri, type, size);
    }

    public Photo (){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
