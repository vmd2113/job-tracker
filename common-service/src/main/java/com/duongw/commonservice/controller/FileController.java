package com.duongw.commonservice.controller;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.commonservice.model.dto.request.file.FileUploadRequest;
import com.duongw.commonservice.model.dto.response.file.FileResponseDTO;
import com.duongw.commonservice.service.IFileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_PATH_VERSION + "/files")
public class FileController {

    private final IFileService fileService;

    public FileController(IFileService fileService) {
        this.fileService = fileService;
    }


    @Operation(summary = "Upload multiple files")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("businessCode") String businessCode,
            @RequestParam("businessId") String businessId) {
        fileService.storeFiles(files, businessCode, businessId);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, "Upload files successfully");
        return ResponseEntity.ok(apiResponse);
    }


    @Operation(summary = "Get all files")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllFiles() {
        List<FileResponseDTO> fileList = fileService.getAllFile();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("file.get-all.success"), fileList);
        return ResponseEntity.ok(apiResponse);


    }

    @Operation(summary = "Get file by ID")
    @GetMapping("/{fileId}")
    public ResponseEntity<ApiResponse<?>> getFile(@PathVariable String fileId) {
        FileResponseDTO file = fileService.getFile(fileId);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("file.get-by-id.success"), file);
        return ResponseEntity.ok(apiResponse);
    }


    @Operation(summary = "Download file by ID")
    @GetMapping("/{fileId}/download")
    public ResponseEntity<ApiResponse<?>> downloadFile(@PathVariable Long fileId) {
        byte[] data = fileService.downloadFile(fileId);
        FileResponseDTO fileInfo = fileService.getFile(fileId.toString());
        ByteArrayResource resource = new ByteArrayResource(data);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("file.download.success"), fileInfo);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileInfo.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(apiResponse);
    }


}
