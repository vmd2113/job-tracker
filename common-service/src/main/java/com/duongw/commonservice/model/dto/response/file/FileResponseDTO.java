package com.duongw.commonservice.model.dto.response.file;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class FileResponseDTO {

    private Long fieldId;

    private String fileName;

    private String filePath;

    private String businessCode;

    private Long businessId;

    private Long status;
}
