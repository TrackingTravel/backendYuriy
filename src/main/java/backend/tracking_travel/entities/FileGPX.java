package backend.tracking_travel.entities;

import javax.persistence.*;

@Entity
@Table(name = "GPX_FILES")
public class FileGPX extends FileResponse {

    public FileGPX (String name, String uri, String type, Long size) {
        super(name, uri, type, size);
    }

    public FileGPX() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
