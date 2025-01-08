package com.duongw.commonservice.service.impl;

import com.duongw.common.config.minio.MinIOConfig;
import com.duongw.common.config.minio.MinioProperties;
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
    private final MinioProperties minioProperties;


    @Autowired
    public MinIOService(MinIOConfig minIOConfig, MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @Override
    public String uploadToMinIO(MultipartFile file, String businessCode) {
        try {
            String filePath = buildFilePath(businessCode, file);

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(filePath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return filePath;
        } catch (Exception e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new FileStorageException("Failed to upload file to MinIO", e);
        }
    }

    private String buildFilePath(String businessCode, MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timePath = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        return String.join("/",
                businessCode,
                datePath,
                timePath + "_" + businessCode + "_" + uniqueId + "." + extension);
    }

    @Override
    public byte[] downloadFromMinIO(String filePath) {
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
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
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(filePath)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting file from MinIO: {}", e.getMessage(), e);
            throw new FileStorageException("Failed to delete file from MinIO", e);
        }
    }

    @Override
    public boolean isFileExist(String filePath) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(filePath)
                            .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
