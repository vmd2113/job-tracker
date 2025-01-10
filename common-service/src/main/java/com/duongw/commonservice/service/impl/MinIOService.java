package com.duongw.commonservice.service.impl;

import com.duongw.common.exception.FileStorageException;
import com.duongw.commonservice.service.IMinIOService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j

public class MinIOService implements IMinIOService {

    private static final String BUCKET_NAME = "job-tracker";
    private final MinioClient minioClient;


    @Autowired
    public MinIOService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String uploadToMinIO(MultipartFile file, String businessCode) {
        try {
            log.info("Uploading file to MinIO: {}", file.getOriginalFilename());
            String filePath = buildFilePath(businessCode, file);


            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(filePath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            log.info("File uploaded to MinIO successfully: {}", filePath);
            return filePath;
        } catch (Exception e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new FileStorageException("Failed to upload file to MinIO", e);
        }
    }

    private String buildFilePath(String businessCode, MultipartFile file) {
        log.info("MinIOService  -> buildFilePath");
        LocalDateTime now = LocalDateTime.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String timePath = now.format(DateTimeFormatter.ofPattern("HH-mmss"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        return String.join("/",
                businessCode,
                datePath,
                datePath+ "_" + timePath + "_" + businessCode + "_" + uniqueId + "." + extension);
    }

    @Override
    public byte[] downloadFromMinIO(String filePath) {
        try {
            log.info("Downloading file from MinIO: {}", filePath.toString());
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(filePath)
                            .build());
            return response.readAllBytes();
        } catch (Exception e) {
            log.error("Error downloading file from MinIO: {}", e.getMessage(), e);
            throw new FileStorageException("Failed to download file from MinIO", e);

        }
    }

    @Override
    public void deleteFromMinIO(String filePath) {
        try {
            log.info("Deleting file from MinIO: {}", filePath);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(filePath)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting file from MinIO: {}", e.getMessage(), e);
            try {
                throw new FileStorageException("Failed to delete file from MinIO", e);
            } catch (FileStorageException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    @Override
    public boolean isFileExist(String filePath) {
        try {
            log.info("Checking if file exists in MinIO: {}", filePath);
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(filePath)
                            .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
