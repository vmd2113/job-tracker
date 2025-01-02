package com.duongw.commonservice.service;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinIOService implements IMinIOService {

    private final MinioClient minioClient;

    @Autowired
    public MinIOService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String uploadFile(MultipartFile file, String businessCode, String businessId) {
        return "";
    }

    @Override
    public byte[] downloadFile(String filePath) {
        return new byte[0];
    }

    @Override
    public void deleteFile(String filePath) {

    }

    @Override
    public boolean isFileExist(String filePath) {
        return false;
    }
}
