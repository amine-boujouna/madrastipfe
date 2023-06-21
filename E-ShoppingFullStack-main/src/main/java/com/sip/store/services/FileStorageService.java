package com.sip.store.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.sip.store.entities.FileDB;
import com.sip.store.repositories.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;
    private final Path root = Paths.get("C:/work/madrastipfe/E-ShoppingFullStack-main/src/uploads");


    public FileDB deletefile(String idfile)
    {
        FileDB temp = null;

        Optional<FileDB> opt = fileDBRepository.findById(idfile);

        if(opt.isPresent())
        {
            temp = opt.get();
            fileDBRepository.delete(temp);

        }
        if(temp == null) throw new IllegalArgumentException("file with id = "+ idfile +"not Found");
        return temp;
    }
    public FileDB update(String fileId, MultipartFile file) throws IOException {
        // Recherche du fichier à mettre à jour dans la base de données
        Optional<FileDB> optionalFileDB = fileDBRepository.findById(fileId);

        if (optionalFileDB.isPresent()) {
            FileDB existingFileDB = optionalFileDB.get();

            // Mise à jour du nom de fichier
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            existingFileDB.setName(fileName);

            // Mise à jour du type de contenu
            existingFileDB.setType(file.getContentType());

            // Mise à jour du contenu du fichier
            existingFileDB.setData(file.getBytes());

            // Enregistrement des modifications dans la base de données
            return fileDBRepository.save(existingFileDB);
        } else {
            throw new FileNotFoundException("Le fichier avec l'ID " + fileId + " n'a pas été trouvé.");
        }
    }

    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}

