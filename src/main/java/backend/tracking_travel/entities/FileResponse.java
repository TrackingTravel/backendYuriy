package backend.tracking_travel.entities;

import lombok.Data;

@Data
public class FileResponse {

    private String name;
    private String uri;
    private String type;
    private Long size;

    public FileResponse(String name, String uri, String type, Long size) {
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.size = size;
    }

    public FileResponse (){}

}
