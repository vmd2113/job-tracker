package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.FileStorageException;
import com.duongw.common.exception.InvalidDataException;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.commonservice.model.dto.request.file.FileUploadRequest;
import com.duongw.commonservice.model.dto.response.file.FileResponseDTO;
import com.duongw.commonservice.model.entity.Files;
import com.duongw.commonservice.repository.FileRepository;
import com.duongw.commonservice.service.IFileService;
import com.duongw.commonservice.service.IMinIOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileService implements IFileService {


    private final FileRepository fileRepository;
    private final IMinIOService minIOService;


    @Autowired
    public FileService(FileRepository fileRepository, IMinIOService minIOService) {
        this.fileRepository = fileRepository;
        this.minIOService = minIOService;
    }


    private FileResponseDTO convertToFileResponseDTO(Files files) {
        log.info("FILE_SERVICE  -> convertToFileResponseDTO");
        log.info("FileResponseDTO: {}", files);
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
        log.info("FILE_SERVICE  -> saveFile");
        Files files = Files.builder()
                .fileName(fileUploadRequest.getFileName())
                .filePath(fileUploadRequest.getFilePath())
                .businessCode(fileUploadRequest.getBusinessCode())
                .businessId(fileUploadRequest.getBusinessId())
                .status(fileUploadRequest.getStatus())
                .build();

        log.info("FILE_SERVICE  -> saveFile success");
        return convertToFileResponseDTO(fileRepository.save(files));
    }


    @Override
    public void storeFiles(List<MultipartFile> files, String businessCode, String businessId) {
        if (files == null || files.isEmpty()) {
            log.info("FILE_SERVICE  -> storeFiles fail because files is empty");
            throw new InvalidDataException("Files cannot be empty");
        }

        for (MultipartFile file : files) {
            // Upload to MinIO and get file path
            String filePath = minIOService.uploadToMinIO(file, businessCode);

            // Save file metadata to database
            FileUploadRequest fileRequest = FileUploadRequest.builder()
                    .fileName(file.getOriginalFilename())
                    .filePath(filePath)
                    .businessCode(businessCode)
                    .businessId(Long.valueOf(businessId))
                    .status(1L)
                    .build();

            saveFile(fileRequest);
            log.info("FILE_SERVICE  -> storeFiles in MinIO success");
        }
    }

    @Override
    public List<FileResponseDTO> getAllFile() {

        List<Files> files = fileRepository.findAll();
        log.info("FILE_SERVICE  -> getAllFile success");
        return files.stream().map(this::convertToFileResponseDTO).collect(Collectors.toList());
    }


    @Override
    public void deleteFilesByBusinessCode(String businessCode) {
        //TODO: delete files by businessCode
        List<Files> files = fileRepository.findByBusinessCode(businessCode);
        if (files == null || files.isEmpty()) {
            for (Files file : files) {

                //TODO: delete file from MinIO
                fileRepository.delete(file);
            }
        }

    }


    @Override
    public FileResponseDTO getFile(Long fileId) {

        Files files = fileRepository.findById(fileId).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("file.not-found")));
        return convertToFileResponseDTO(files);
    }


    @Override
    public List<FileResponseDTO> getFileByBusinessCode(String businessCode) {
        return null;
    }

    @Override
    public byte[] downloadFile(String path) {
        return new byte[0];
    }

    @Override
    public byte[] downloadFileById(Long fileId) {
        try {
            log.info("Downloading file by ID: {}", fileId);
            Files fileInfo = fileRepository.findById(fileId)
                    .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

            return minIOService.downloadFromMinIO(fileInfo.getFilePath());
        } catch (FileNotFoundException e) {
            log.error("File not found with ID {}: {}", fileId, e.getMessage());
            throw new FileStorageException("File not found with id: " + fileId, e);
        } catch (Exception e) {
            log.error("Error downloading file by ID {}: {}", fileId, e.getMessage(), e);
            throw new FileStorageException("Failed to download file by ID", e);
        }
    }

    @Override
    public void deleteFileById(Long fileId) {
        try {
            log.info("Deleting file by ID: {}", fileId);
            Files fileInfo = fileRepository.findById(fileId)
                    .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

            // Delete from MinIO
            minIOService.deleteFromMinIO(fileInfo.getFilePath());

            // Delete from database
            fileRepository.delete(fileInfo);
            log.info("File deleted successfully, ID: {}", fileId);
        } catch (FileNotFoundException e) {
            log.error("File not found with ID {}: {}", fileId, e.getMessage());
            throw new FileStorageException("File not found with id: " + fileId, e);
        } catch (Exception e) {
            log.error("Error deleting file by ID {}: {}", fileId, e.getMessage(), e);
            throw new FileStorageException("Failed to delete file by ID", e);
        }

    }
}
