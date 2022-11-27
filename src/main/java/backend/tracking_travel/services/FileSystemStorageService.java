package backend.tracking_travel.services;

import backend.tracking_travel.config.StorageProperties;
import backend.tracking_travel.entities.FileGPX;
import backend.tracking_travel.entities.MapPhoto;
import backend.tracking_travel.entities.Photo;
import backend.tracking_travel.entities.TestRoute;
import backend.tracking_travel.exeptions.FileNotFoundException;
import backend.tracking_travel.exeptions.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath().normalize();
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return filename;
    }

    public FileGPX storeGPX(MultipartFile file) {

        String name = store(file);

        String uri = ServletUriComponentsBuilder.fromHttpUrl("https://api.trackingtravel.me")
                .path("/gpx/download/")
                .path(name)
                .toUriString();
        return new FileGPX(name, uri, file.getContentType(), file.getSize());
    }

    public List<FileGPX> multiStoreGPX(MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::storeGPX)
                .collect(Collectors.toList());
    }

    public Photo storePhoto(MultipartFile file) {
        String name = store(file);

        String uri = ServletUriComponentsBuilder.fromHttpUrl("https://api.trackingtravel.me")
                // в uri пишем путь чтобы он совпадал с запросом скачивания в api
                .path("/photo/download/")
                .path(name)
                .toUriString();
        return new Photo(name, uri, file.getContentType(), file.getSize());
    }

    public List<Photo> multiStorePhoto(MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> storePhoto(file))
                .collect(Collectors.toList());
    }

    public MapPhoto storeMapPhoto(MultipartFile file){
        String name = store(file);

        String uri = ServletUriComponentsBuilder.fromHttpUrl("https://api.trackingtravel.me")
                .path("/mapPhoto/download/")
                .path(name)
                .toUriString();
        return new MapPhoto(name, uri, file.getContentType(), file.getSize());
    }

    public List<MapPhoto> multiStoreMapPhoto(MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> storeMapPhoto(file))
                .collect(Collectors.toList());
    }



    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = this.rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
