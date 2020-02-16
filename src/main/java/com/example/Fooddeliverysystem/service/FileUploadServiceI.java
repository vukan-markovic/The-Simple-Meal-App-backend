package com.example.Fooddeliverysystem.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadServiceI {

    String uploadFile(MultipartFile uploadfile, String fileType);

    Resource getFile(String imageName);
}