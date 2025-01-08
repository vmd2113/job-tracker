package com.duongw.commonservice.controller;

import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.commonservice.model.dto.response.file.FileResponseDTO;
import com.duongw.commonservice.service.IFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_FILE)
public class FileController {


    private final IFileService fileService;

    public FileController(IFileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = "/upload")
    public ResponseEntity<ApiResponse<?>> uploadAndStoreFile(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("code") String businessCode,
            @RequestPart("id") String businessId
    ) {

        fileService.storeFiles(files, businessCode, businessId);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, "Upload and store file successfully", null);
        return ResponseEntity.ok(apiResponse);

    }



}
