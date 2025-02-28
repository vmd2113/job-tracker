package com.duongw.workforceservice.validator;

import com.duongw.common.exception.AlreadyExistedException;
import com.duongw.common.exception.InvalidDataException;
import com.duongw.workforceservice.model.dto.resquest.worktype.CreateWorkTypeRequest;
import com.duongw.workforceservice.model.dto.resquest.worktype.UpdateWorkTypeRequest;
import com.duongw.workforceservice.model.entity.WorkType;
import com.duongw.workforceservice.repository.WorkTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkTypeValidator {

    private final WorkTypeRepository workTypeRepository;

    @Autowired
    public WorkTypeValidator(WorkTypeRepository workTypeRepository) {
        this.workTypeRepository = workTypeRepository;
    }

    public void validateCreateWorkType(CreateWorkTypeRequest request) {
        // Validate required fields
        validateRequiredFields(request.getWorkTypeCode(), request.getWorkTypeName(),
                request.getProcessTime(), request.getPriorityId());

        // Validate duplicate workTypeCode and workTypeName
        validateDuplicateWorkType(null, request.getWorkTypeCode(), request.getWorkTypeName());

        // Validate processTime
        validateProcessTime(request.getProcessTime());

        // Validate status
        validateStatus(request.getStatus());
    }

    public void validateUpdateWorkType(Long workTypeId, UpdateWorkTypeRequest request) {
        // Validate required fields
        validateRequiredFields(request.getWorkTypeCode(), request.getWorkTypeName(),
                request.getProcessTime(), request.getPriorityId());

        // Validate duplicate workTypeCode and workTypeName
        validateDuplicateWorkType(workTypeId, request.getWorkTypeCode(), request.getWorkTypeName());

        // Validate processTime
        validateProcessTime(request.getProcessTime());

        // Validate status
        validateStatus(request.getStatus());
    }

    private void validateRequiredFields(String workTypeCode, String workTypeName, Float processTime, Long priorityId) {
        if (workTypeCode == null || workTypeCode.trim().isEmpty()) {
            throw new InvalidDataException("Mã loại công việc không được để trống");
        }
        if (workTypeName == null || workTypeName.trim().isEmpty()) {
            throw new InvalidDataException("Tên loại công việc không được để trống");
        }
        if (processTime == null) {
            throw new InvalidDataException("Thời gian xử lý không được để trống");
        }
//        if (priorityId == null) {
//            throw new InvalidDataException("Mức độ ưu tiên không được để trống");
//        }
    }

    private void validateDuplicateWorkType(Long currentWorkTypeId, String workTypeCode, String workTypeName) {
        // Check workTypeCode
        WorkType existingWorkTypeByCode = workTypeRepository.findByWorkTypeCode(workTypeCode);
        if (existingWorkTypeByCode != null &&
                (currentWorkTypeId == null || !existingWorkTypeByCode.getWorkTypeId().equals(currentWorkTypeId))) {
            throw new AlreadyExistedException("Mã loại công việc đã tồn tại");
        }

        // Check workTypeName
        WorkType existingWorkTypeByName = workTypeRepository.findByWorkTypeName(workTypeName);
        if (existingWorkTypeByName != null &&
                (currentWorkTypeId == null || !existingWorkTypeByName.getWorkTypeId().equals(currentWorkTypeId))) {
            throw new AlreadyExistedException("Tên loại công việc đã tồn tại");
        }
    }

    private void validateProcessTime(Float processTime) {
        if (processTime != null) {
            if (processTime <= 0) {
                throw new InvalidDataException("Thời gian xử lý phải lớn hơn 0");
            }
            // Optional: Add more validation for maximum process time if needed
            if (processTime > 720) { // Example: max 30 days (720 hours)
                throw new InvalidDataException("Thời gian xử lý không được vượt quá 720 giờ");
            }
        }
    }

    private void validateStatus(Long status) {
        if (status != null && (status < 0 || status > 1)) {
            throw new InvalidDataException("Trạng thái không hợp lệ");
        }
    }
}