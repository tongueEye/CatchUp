package com.catchup.catchup.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.JsonObject;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
public class FileManageController {
    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping(value = "/file/uploadFile", produces = "application/json")
    @ResponseBody
    public JsonObject uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {

        JsonObject jsonObject = new JsonObject();

        String originalFileName = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFileName);
        String savedFileName = UUID.randomUUID().toString() + "." + extension;

        try {
            // S3에 업로드
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            metadata.setContentLength(multipartFile.getSize());

            amazonS3.putObject(new PutObjectRequest(bucket, savedFileName, multipartFile.getInputStream(), metadata));

            // URL 설정
            String imageUrl = amazonS3.getUrl(bucket, savedFileName).toString();
            jsonObject.addProperty("url", imageUrl);
            jsonObject.addProperty("responseCode", "success");


        } catch (IOException e) {
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }// 썸머노트 이미지 S3 저장
}
