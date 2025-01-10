package com.duongw.commonservice.service;

import com.duongw.commonservice.model.dto.request.file.FileUploadRequest;
import com.duongw.commonservice.model.dto.response.file.FileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {

    void storeFiles(List<MultipartFile> files, String businessCode, String businessId);

    FileResponseDTO saveFile(FileUploadRequest fileUploadRequest);

    List<FileResponseDTO> getAllFile();

    FileResponseDTO getFile(Long fileId);

    List<FileResponseDTO> getFileByBusinessCode(String businessCode);

    byte[] downloadFile(String path);

    void deleteFilesByBusinessCode(String businessCode);

    byte[] downloadFileById(Long fileId);

    void deleteFileById(Long fileId);
}
