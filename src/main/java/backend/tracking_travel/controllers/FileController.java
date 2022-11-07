package backend.tracking_travel.controllers;

import backend.tracking_travel.entities.FileGPX;
import backend.tracking_travel.entities.Photo;
import backend.tracking_travel.services.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/storage")
public class FileController {

    private static StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/getListFiles")
    @Operation(summary = "Вывод списка всех загруженных файлов", description = "Вывод списка всех загруженных файлов")
    public String listAllFiles(Model model) {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/download/*/")
                                .path(path.getFileName().toString())
                                .toUriString())
                .collect(Collectors.toList()));
        return "listFiles";
    }

    @GetMapping("/download/{filename:.+}")
    @Operation(summary = "Скачивание файла", description = "Позволяет скачать файла с сервера")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {

        Resource resource = storageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/upload-gpx")
    @Operation(summary = "Загрузка файла gpx на сервер", description = "Позволяет загрузить файл gpx на сервер")
    public void uploadGpx(@RequestParam("gpx") MultipartFile file) {

        storageService.storeGPX(file);
    }

    @PostMapping("/upload-photo")
    @Operation(summary = "Загрузка фотографии на сервер", description = "Позволяет загрузить фотографию на сервер")
    public void uploadPhoto(@RequestParam("photo") MultipartFile file) {

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