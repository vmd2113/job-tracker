package com.duongw.commonservice.model.dto.request.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadRequest {

    private Long fieldId;

    private String fileName;

    private String filePath;

    private String businessCode;

    private String businessId;

    private Long status;

}
