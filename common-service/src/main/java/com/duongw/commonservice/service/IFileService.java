package com.duongw.commonservice.service;

import com.duongw.commonservice.model.dto.response.file.FileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {

    void storeFiles(List<MultipartFile> files, String businessCode, String businessId);
    List<FileResponseDTO> searchFileByBusinessID(String businessCode, String businessId);
    void deleteFilesByBusinessId(String businessId);
    void deleteFilesByBusinessCode(String businessCode);
    FileResponseDTO getFile(String fileId);
    FileResponseDTO getFileByBusinessId(String businessId);
    FileResponseDTO getFileByBusinessCode(String businessCode);
    void downloadFile(Long fileId);
    void downloadFileByBusinessId(String businessId);
    void downloadFileByBusinessCode(String businessCode);



}
