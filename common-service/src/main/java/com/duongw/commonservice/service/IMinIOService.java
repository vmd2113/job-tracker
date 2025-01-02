package com.duongw.commonservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface IMinIOService {

    String uploadFile(MultipartFile file, String businessCode, String businessId);
    byte[] downloadFile(String filePath);
    void deleteFile(String filePath);
    boolean isFileExist(String filePath);
}
