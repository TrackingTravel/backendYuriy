package backend.tracking_travel.services;

import backend.tracking_travel.entities.FileGPX;
import backend.tracking_travel.entities.MapPhoto;
import backend.tracking_travel.entities.Photo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    FileGPX storeGPX (MultipartFile file);
    List<FileGPX> multiStoreGPX (MultipartFile[] files);

    Photo storePhoto (MultipartFile file);
    List<Photo> multiStorePhoto (MultipartFile[] files);

    MapPhoto storeMapPhoto (MultipartFile file);


    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
