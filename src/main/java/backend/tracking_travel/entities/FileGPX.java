package backend.tracking_travel.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "GPX_FILES")
@Data
public class FileGPX {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String filename;
    private String type;
    private Long size;

    public FileGPX() {}

    public FileGPX(String name, String filename, String type, Long size) {
        this.name = name;
        this.filename = filename;
        this.type = type;
        this.size = size;
    }
}
