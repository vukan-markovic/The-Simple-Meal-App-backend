package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.service.FileUploadServiceI;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("file")
public class FileUploadController {

    private FileUploadServiceI fileUploadService;

    @PostMapping("/{fileType}")
    public ResponseEntity<JSONObject> uploadImage(@PathVariable("fileType") String fileType, @RequestBody MultipartFile file) {
        String result = fileUploadService.uploadFile(file, fileType);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image", result);
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Resource> getImage(@RequestParam String imagePath) {
        Resource resource = fileUploadService.getFile(imagePath);
        if (resource == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @Autowired
    public void setFileUploadService(FileUploadServiceI fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

}