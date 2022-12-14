package backend.tracking_travel.controllers;

import backend.tracking_travel.entities.Photo;
import backend.tracking_travel.entities.TestRoute;
import backend.tracking_travel.exeptions.FileNotFoundException;
import backend.tracking_travel.repo.PhotoRepository;
import backend.tracking_travel.repo.TestRoutesRepository;
import backend.tracking_travel.services.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
public class FileController {
    static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final StorageService storageService;
    private final PhotoRepository photoRepository;
    private final TestRoutesRepository testRoutesRepository;

    @Autowired
    public FileController(StorageService storageService, PhotoRepository photoRepository, TestRoutesRepository testRoutesRepository) {
        this.storageService = storageService;
        this.photoRepository = photoRepository;
        this.testRoutesRepository = testRoutesRepository;
    }

    @GetMapping(value = "/photo")
    @Operation(summary = "Запрос всех фотографий маршрутов", description = "Позволяет запросить все фотографии маршрутов из БД")
    public ResponseEntity<List<Photo>> getAllPhoto() {
        final List<Photo> photo = photoRepository.findAll();
        return !photo.isEmpty()
                ? new ResponseEntity<>(photo, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/photoByRouteId/{id}")
    @Operation(summary = "Запрос фотографий по ID маршрута", description = "Позволяет запросить фотографии по ID маршрута из БД")
    public ResponseEntity<List<Photo>> getPhotoByRouteId(@PathVariable(name = "id") Long id) {
        final Optional<TestRoute> optionalRoute = testRoutesRepository.findById(id);
        if (optionalRoute.isPresent()) {
            final List<Photo> routePhoto = optionalRoute.get().getPhoto();
            return new ResponseEntity<>(routePhoto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/log")
    @Operation(summary = "Скачивание файла лога", description = "Позволяет скачать файл лога с сервера")
    public ResponseEntity<Resource> downloadLogFile() throws MalformedURLException {
        Path file = Paths.get("./application.log").normalize();
        Resource resource = new UrlResource(file.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @GetMapping("/photo/download/{filename:.+}")
    @Operation(summary = "Скачивание фото", description = "Позволяет скачать фото с сервера")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable String filename, HttpServletRequest request) {

        Resource resource = storageService.loadAsResource(filename);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Не удалось определить тип файла.");
        }

        // Возврат к типу контента по умолчанию, если тип не может быть определен
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/mapPhoto/download/{filename:.+}")
    @Operation(summary = "Скачивание скриншота карты", description = "Позволяет скачать скриншот карты с сервера")
    public ResponseEntity<Resource> downloadMapPhoto(@PathVariable String filename, HttpServletRequest request) {

        Resource resource = storageService.loadAsResource(filename);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Не удалось определить тип файла.");
        }

        // Возврат к типу контента по умолчанию, если тип не может быть определен
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/upload-gpx")
    @Operation(summary = "Загрузка файла gpx на сервер", description = "Позволяет загрузить файл gpx на сервер")
    public void uploadGpx(@RequestParam("gpx") MultipartFile file) {
        storageService.storeGPX(file);
    }

    @PostMapping("/upload-photo")
    @Operation(summary = "Загрузка фотографии на сервер", description = "Позволяет загрузить фотографию на сервер")
    public void uploadPhoto(@RequestParam("photo") MultipartFile file) throws IOException {
        storageService.storePhoto(file);
    }

    @PostMapping("/upload-multiple-gpx")
    @Operation(summary = "Множественная загрузка файлов gpx на сервер", description = "Позволяет загрузить сразу несколько файлов gpx на сервер")
    public void multiUploadGpx(@RequestParam("files") MultipartFile[] files) {
        storageService.multiStoreGPX(files);
    }

    @PostMapping("/upload-multiple-photo")
    @Operation(summary = "Множественная загрузка фотографий на сервер", description = "Позволяет загрузить сразу несколько фотографий на сервер")
    public void uploadMultiplePhoto(@RequestParam("files") MultipartFile[] files) {
        storageService.multiStorePhoto(files);
    }
}