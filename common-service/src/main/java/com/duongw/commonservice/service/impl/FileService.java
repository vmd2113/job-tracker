package com.duongw.commonservice.service.impl;

import com.duongw.commonservice.model.dto.response.file.FileResponseDTO;
import com.duongw.commonservice.service.IFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService implements IFileService  {
    @Override
    public void storeFiles(List<MultipartFile> files, String businessCode, String businessId) {

    }

    @Override
    public List<FileResponseDTO> searchFileByBusinessID(String businessCode, String businessId) {
        return List.of();
    }

    @Override
    public void deleteFilesByBusinessId(String businessId) {

    }

    @Override
    public void deleteFilesByBusinessCode(String businessCode) {

    }

    @Override
    public FileResponseDTO getFile(String fileId) {
        return null;
    }

    @Override
    public FileResponseDTO getFileByBusinessId(String businessId) {
        return null;
    }

    @Override
    public FileResponseDTO getFileByBusinessCode(String businessCode) {
        return null;
    }

    @Override
    public void downloadFile(Long fileId) {

    }

    @Override
    public void downloadFileByBusinessId(String businessId) {

    }

    @Override
    public void downloadFileByBusinessCode(String businessCode) {

    }
}
