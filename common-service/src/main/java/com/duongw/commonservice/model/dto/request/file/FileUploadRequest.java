package com.duongw.commonservice.model.dto.request.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadRequest {

    private Long fieldId;

    private String fileName;

    private String filePath;

    private String businessCode;

    private Long businessId;

    private Long status = 1L;

    public FileUploadRequest(String fileName, String s, String businessCode, Object o) {
    }
}
