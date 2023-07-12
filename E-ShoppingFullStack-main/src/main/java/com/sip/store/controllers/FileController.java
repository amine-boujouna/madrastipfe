package com.sip.store.controllers;

import com.sip.store.entities.FileDB;
import com.sip.store.entities.FileInfo;
import com.sip.store.entities.ResponseFile;
import com.sip.store.entities.ResponseMessage;
import com.sip.store.repositories.FileDBRepository;
import com.sip.store.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class FileController {
    @Autowired
    private FileStorageService storageService;
    @Autowired
    FileDBRepository fileDBRepository;


    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.store(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @GetMapping("/Files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }
    @GetMapping("/getallfiledb")
    public List<FileDB> getAllfiledb() {
        return storageService.getAllFilesdb();

    }
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileDB fileDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }
    @GetMapping("/reglements/{id}")
    public ResponseEntity<byte[]> getfile(@PathVariable String id) {
        FileDB fileDB = storageService.getfile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }
    @DeleteMapping("/Delete/{id}")
    public FileDB deleteFile(@PathVariable String id) {
        return storageService.deletefile(id);
    }
    @PutMapping("/update/{fileId}")
    public ResponseEntity<FileDB> updateFile(@PathVariable String fileId, @RequestParam("file") MultipartFile file) {
        try {
            FileDB updatedFile = storageService.update(fileId, file);
            return ResponseEntity.ok(updatedFile);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (FileSystemNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/Getemplois")
    public List<FileInfo> GetEmplois() {
        return storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());
    }
    @GetMapping("/GetReglements")
    public List<FileInfo> Getreglements() {
        return storageService.loadAllreglements().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getfile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());
    }

}
