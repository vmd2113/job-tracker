package com.duongw.commonservice.service;

import com.duongw.commonservice.model.dto.request.file.FileUploadRequest;
import com.duongw.commonservice.model.dto.response.file.FileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {
    FileResponseDTO saveFile(FileUploadRequest fileUploadRequest);

    void storeFiles(List<MultipartFile> files, String businessCode, String businessId);

    List<FileResponseDTO> getAllFile();

    List<FileResponseDTO> searchFileByBusinessID(String businessCode, String businessId);

    void deleteFilesByBusinessId(Long businessId);

    void deleteFilesByBusinessCode(String businessCode);

    FileResponseDTO getFile(String fileId);

    FileResponseDTO getFileByBusinessId(Long businessId);

    FileResponseDTO getFileByBusinessCode(String businessCode);

    byte[] downloadFile(Long fileId);


}
