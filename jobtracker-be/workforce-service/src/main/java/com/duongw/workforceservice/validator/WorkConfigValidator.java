package com.duongw.workforceservice.validator;

import com.duongw.common.exception.InvalidDataException;
import com.duongw.workforceservice.model.dto.resquest.workconfig.CreateWorkConfigRequest;
import com.duongw.workforceservice.model.dto.resquest.workconfig.UpdateWorkConfigRequest;
import com.duongw.workforceservice.model.entity.WorkConfigBusiness;
import com.duongw.workforceservice.repository.WorkConfigBusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class WorkConfigValidator {

    private final WorkConfigBusinessRepository workConfigBusinessRepository;

    @Autowired
    public WorkConfigValidator(WorkConfigBusinessRepository workConfigBusinessRepository) {
        this.workConfigBusinessRepository = workConfigBusinessRepository;
    }

    public void validateCreateWorkConfig(CreateWorkConfigRequest request) {
        // Validate required fields
        validateRequiredFields(request.getWorkTypeId(), request.getPriorityId(),
                request.getOldStatusId(), request.getNewStatusId());

        // Validate status transition
        validateStatusTransition(request.getOldStatusId(), request.getNewStatusId());

        // Check for duplicate configuration
        validateDuplicateConfig(null, request.getWorkTypeId(), request.getPriorityId(),
                request.getOldStatusId(), request.getNewStatusId());
    }

    public void validateUpdateWorkConfig(Long configId, UpdateWorkConfigRequest request) {
        // Validate required fields
        validateRequiredFields(request.getWorkTypeId(), request.getPriorityId(),
                request.getOldStatusId(), request.getNewStatusId());

        // Validate status transition
        validateStatusTransition(request.getOldStatusId(), request.getNewStatusId());

        // Check for duplicate configuration
        validateDuplicateConfig(configId, request.getWorkTypeId(), request.getPriorityId(),
                request.getOldStatusId(), request.getNewStatusId());
    }

    private void validateRequiredFields(Long workTypeId, Long priorityId, Long oldStatus, Long newStatus) {
        if (workTypeId == null) {
            throw new InvalidDataException("Loại công việc không được để trống");
        }
        if (priorityId == null) {
            throw new InvalidDataException("Mức độ ưu tiên không được để trống");
        }
        if (oldStatus == null) {
            throw new InvalidDataException("Trạng thái cũ không được để trống");
        }
        if (newStatus == null) {
            throw new InvalidDataException("Trạng thái mới không được để trống");
        }
    }

    private void validateStatusTransition(Long oldStatus, Long newStatus) {
        if (Objects.equals(oldStatus, newStatus)) {
            throw new InvalidDataException("Trạng thái mới phải khác trạng thái cũ");
        }
    }

    private void validateDuplicateConfig(Long currentConfigId, Long workTypeId, Long priorityId,
                                         Long oldStatus, Long newStatus) {
        List<WorkConfigBusiness> existingConfigs = workConfigBusinessRepository.findAll();

        for (WorkConfigBusiness config : existingConfigs) {
            // Skip current config when updating
            if (currentConfigId != null && config.getWorkConfigId().equals(currentConfigId)) {
                continue;
            }

            // Check if all 4 fields match
            boolean isDuplicate = config.getWorkType().getWorkTypeId().equals(workTypeId) &&
                    config.getPriorityId().equals(priorityId) &&
                    config.getOldStatus().equals(oldStatus) &&
                    config.getNewStatus().equals(newStatus);

            if (isDuplicate) {
                throw new InvalidDataException(
                        "Tồn tại bản ghi trùng thông tin Tên loại công việc, Mức độ ưu tiên, Trạng thái cũ, Trạng thái mới."
                );
            }
        }
    }
}