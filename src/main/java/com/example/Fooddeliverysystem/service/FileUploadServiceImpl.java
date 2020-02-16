package com.example.Fooddeliverysystem.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadServiceImpl implements FileUploadServiceI {


    private static final String WEEKLY_MENU_PATH = "./src/main/webapp/uploads/weeklyMenuImages/";
    private static final String AVATAR_PATH = "./src/main/webapp/uploads/avatarImages/";

    @Override
    public String uploadFile(MultipartFile uploadfile, String fileType) {
        String url;
        switch (fileType.toUpperCase()) {
            case "WEEKLY-MENU":
                url = WEEKLY_MENU_PATH;
                break;
            case "AVATAR":
                url = AVATAR_PATH;
                break;
            default:
                return null;
        }
        Path filePath = Paths.get(url, uploadfile.getOriginalFilename());

        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            outputStream.write(uploadfile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return url + uploadfile.getOriginalFilename();
    }

    @Override
    public Resource getFile(String imagePath) {
        try {
            return new InputStreamResource(new FileInputStream(imagePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}