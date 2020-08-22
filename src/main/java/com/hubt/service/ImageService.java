package com.hubt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class ImageService {

    private final String root = "C://Users//Administrator//Desktop//do-an//do-an-hubt//src//main//webapp//WEB-INF//images";


    public void readImage(MultipartFile multipartFile){
        if (multipartFile.getOriginalFilename().toLowerCase().contains("jpg")
                || multipartFile.getOriginalFilename().toLowerCase().contains("jpeg")
                || multipartFile.getOriginalFilename().toLowerCase().contains("png")) {
            Path path = Paths.get(root, multipartFile.getOriginalFilename());
            try(OutputStream os = Files.newOutputStream(path)) {
                os.write(multipartFile.getBytes());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
