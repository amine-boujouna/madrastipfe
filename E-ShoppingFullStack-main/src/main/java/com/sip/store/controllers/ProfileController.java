package com.sip.store.controllers;

import com.sip.store.entities.User;
import com.sip.store.exception.ResourceNotFoundException;
import com.sip.store.repositories.UserRepository;
import com.sip.store.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class ProfileController {
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    FileStorageService storageService;

    @GetMapping("/getuserbyusername/{username}")
    public User finduserbyUsername(@PathVariable String username){
       return userRepository.findByUsername(username);
    }
    @PutMapping("/profile/{id}")
    public User updateProfile(@PathVariable("id") long id, @RequestBody User Profile) {
        User profile=null;
        Optional<User> profileinfo = userRepository.findById(id);
        if (profileinfo.isPresent()) {
            profile = profileinfo.get();
            profile.setNom(Profile.getNom());
            profile.setPrenom(Profile.getPrenom());
            profile.setNompere(Profile.getNompere());
            profile.setNommere(Profile.getNommere());
            profile.setEmail(Profile.getEmail());
            profile.setSpecialite(Profile.getSpecialite());
            profile.setBiographie(Profile.getBiographie());
            profile.setTel(Profile.getTel());
            profile.setAdresse(Profile.getAdresse());
            profile.setAge(Profile.getAge());
            userRepository.save(profile);
        }
        if(profile == null) throw new IllegalArgumentException("profile with id = "+ id +"not Found");
        return profile;
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getProfileById(@PathVariable(value = "id") Long Id)
            throws ResourceNotFoundException {
        User profile = userRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Profrile not found for this id :: " + Id));
        return ResponseEntity.ok().body(profile);
    }


    @GetMapping("/file/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadd(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
