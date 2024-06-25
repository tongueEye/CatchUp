package com.catchup.catchup.controller;

import com.catchup.catchup.service.AWSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
@Slf4j
public class AWSController {
    private final AWSService S3Service;

    @PostMapping("/uploadImageFile")
    public ResponseEntity<List<String>> uploadFile(List<MultipartFile> multipartFiles){

        log.info("test >>>>>> {}", multipartFiles);

        return ResponseEntity.ok(S3Service.uploadFile(multipartFiles));
    }

    @DeleteMapping("/deleteImageFile")
    public ResponseEntity<String> deleteFile(@RequestParam String fileName){
        S3Service.deleteFile(fileName);
        return ResponseEntity.ok(fileName);
    }
}

