package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.InvalidDataException;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.commonservice.model.dto.request.file.FileUploadRequest;
import com.duongw.commonservice.model.dto.response.file.FileResponseDTO;
import com.duongw.commonservice.model.entity.Files;
import com.duongw.commonservice.repository.FileRepository;
import com.duongw.commonservice.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService implements IFileService {


    private final FileRepository fileRepository;


    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    private FileResponseDTO convertToFileResponseDTO(Files files) {
        return FileResponseDTO.builder()
                .fieldId(files.getFieldId())
                .fileName(files.getFileName())
                .filePath(files.getFilePath())
                .businessCode(files.getBusinessCode())
                .businessId(files.getBusinessId())
                .status(files.getStatus())
                .build();


    }

    @Override
    public FileResponseDTO saveFile(FileUploadRequest fileUploadRequest) {
        Files files = Files.builder()
                .fileName(fileUploadRequest.getFileName())
                .filePath(fileUploadRequest.getFilePath())
                .businessCode(fileUploadRequest.getBusinessCode())
                .businessId(fileUploadRequest.getBusinessId())
                .status(fileUploadRequest.getStatus())
                .build();
        return convertToFileResponseDTO(fileRepository.save(files));
    }

    @Override
    public void storeFiles(List<MultipartFile> files, String businessCode, String businessId) {
        //TODO: store files by
        if (files.isEmpty() || files == null) {
            throw new InvalidDataException("File is empty");
        }
        for (MultipartFile file : files) {





        }
    }

    @Override
    public List<FileResponseDTO> getAllFile() {
        List<Files> files = fileRepository.findAll();
        return files.stream().map(this::convertToFileResponseDTO).toList();

    }

    @Override
    public List<FileResponseDTO> searchFileByBusinessID(String businessCode, String businessId) {
        return List.of();
    }

    @Override
    public void deleteFilesByBusinessId(Long businessId) {
        Files files = fileRepository.findByBusinessId(businessId);
        if (files == null) {
            throw new ResourceNotFoundException(Translator.toLocate("file.not-found"));
        }
        fileRepository.delete(files);

    }

    @Override
    public void deleteFilesByBusinessCode(String businessCode) {
        Files files = fileRepository.findByBusinessCode(businessCode);
        if (files == null) {
            throw new ResourceNotFoundException(Translator.toLocate("file.not-found"));
        }
        fileRepository.delete(files);

    }

    @Override
    public FileResponseDTO getFile(String fileId) {
        Files files = fileRepository.findById(Long.parseLong(fileId)).orElseThrow(()-> new ResourceNotFoundException(Translator.toLocate("file.not-found")));
        return convertToFileResponseDTO(files);
    }

    @Override
    public FileResponseDTO getFileByBusinessId(Long businessId) {
        Files files = fileRepository.findByBusinessId(businessId);
        if (files == null) {
            throw new ResourceNotFoundException(Translator.toLocate("file.not-found"));
        }
        return convertToFileResponseDTO(files);
    }

    @Override
    public FileResponseDTO getFileByBusinessCode(String businessCode) {
        Files files = fileRepository.findByBusinessCode(businessCode);
        if (files == null) {
            throw new ResourceNotFoundException(Translator.toLocate("file.not-found"));
        }
        return convertToFileResponseDTO(files);
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
