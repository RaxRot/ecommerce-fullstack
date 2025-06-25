package com.raxrot.back.service.impl;

import com.raxrot.back.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        //File name of current file
        log.debug("Uploading image to path: {}", path);
        String originalFilename = image.getOriginalFilename();
        log.debug("Original filename: {}", originalFilename);

        //Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        // Полный путь до файла: images/uuid.jpg
        String filePath = path + File.separator + fileName;

        //Check if path exist and create
        File folder = new File(path);
        if (!folder.exists()) {
            boolean created = folder.mkdir();
            if (created) {
                log.info("Created image folder: {}", path);
            } else {
                log.error("Failed to create folder: {}", path);
            }
        }

        //Upload to server
        Files.copy(image.getInputStream(), Paths.get(filePath));
        log.info("File saved at: {}", filePath);

        //return file
        return fileName;
    }
}
