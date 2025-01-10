package com.duongw.commonservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface IMinIOService {

    String uploadToMinIO(MultipartFile file, String businessCode);
    byte[] downloadFromMinIO(String filePath);
    void deleteFromMinIO(String filePath);
    boolean isFileExist(String filePath);

}
