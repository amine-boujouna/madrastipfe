package com.sip.store.controllers;

import com.sip.store.entities.FileInfo;
import com.sip.store.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EmploisController {
    @Autowired
    FileStorageService storageService;

    @GetMapping("/emplois")
    public String newFile(Model model) {
        return "emplois/addemplois";
    }

    @PostMapping("/ajouteremplois")
    public String uploadFile(Model model, @RequestParam("file") MultipartFile file) {
        String message = "";

        try {
            storageService.save(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            model.addAttribute("message", message);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            model.addAttribute("message", message);
        }

        return "emplois/addemplois";
    }
    @GetMapping("/getemplois")
    public String getListFiles(Model model) {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(EmploisController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        model.addAttribute("files", fileInfos);

        return "emplois/listemplois";
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = (Resource) storageService.load(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getClass() + "\"").body(file);
    }

}
