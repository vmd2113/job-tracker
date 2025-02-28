package com.duongw.workforceservice.validator;

import com.duongw.common.exception.InvalidDataException;
import com.duongw.workforceservice.model.dto.resquest.works.CreateWorkRequest;
import com.duongw.workforceservice.model.dto.resquest.works.UpdateWorkRequest;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
public class WorksValidator {

    // Định nghĩa giờ làm việc
    private static final LocalTime WORK_START_TIME = LocalTime.of(8, 0); // 8:00 AM
    private static final LocalTime WORK_END_TIME = LocalTime.of(17, 30); // 5:30 PM

    // Danh sách ngày nghỉ lễ (có thể configure từ database hoặc properties)
    private static final List<String> HOLIDAYS = Arrays.asList(
            "2024-01-01", // Năm mới
            "2024-02-10" // Tết Nguyên Đán
            // Thêm các ngày nghỉ khác
    );


    public void validateCreateWork(CreateWorkRequest request) {
        validateStartTime(request.getStartTime());
    }

    public void validateUpdateWork(UpdateWorkRequest request) {
        validateUpdateStartTime(request.getStartTime());
        validateEndTime(request.getStartTime(), request.getEndTime());

        if (request.getFinishTime() != null) {
            validateFinishTime(request.getStartTime(), request.getEndTime(), request.getFinishTime());
        }
    }

    private void validateStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            throw new InvalidDataException("Thời gian bắt đầu không được để trống");
        }

        LocalDateTime now = LocalDateTime.now();
        if (startTime.isBefore(now)) {
            throw new InvalidDataException("Thời gian bắt đầu phải lớn hơn thời gian hiện tại");
        }
    }

    private void validateUpdateStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            throw new InvalidDataException("Thời gian bắt đầu không được để trống");
        }
    }

    private void validateEndTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime == null) {
            throw new InvalidDataException("Thời gian kết thúc không được để trống");
        }

        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            throw new InvalidDataException("Thời gian kết thúc phải lớn hơn thời gian bắt đầu");

        }
    }

    private void validateFinishTime(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime finishTime) {
        if (finishTime.isBefore(startTime)) {
            throw new InvalidDataException("Thời gian hoàn thành không được nhỏ hơn thời gian bắt đầu");
        }
    }

    private void validateWorkingTime(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();

        if (time.isBefore(WORK_START_TIME) || time.isAfter(WORK_END_TIME)) {
            throw new InvalidDataException(
                    String.format("Thời gian làm việc phải trong khoảng %s - %s",
                            WORK_START_TIME.toString(),
                            WORK_END_TIME.toString())
            );
        }
    }

    private void validateWorkingDay(LocalDateTime dateTime) {
        // Kiểm tra cuối tuần
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new InvalidDataException("Không thể tạo/cập nhật công việc vào ngày cuối tuần");
        }

        // Kiểm tra ngày nghỉ lễ
        String dateStr = dateTime.toLocalDate().toString();
        if (HOLIDAYS.contains(dateStr)) {
            throw new InvalidDataException("Không thể tạo/cập nhật công việc vào ngày nghỉ lễ");
        }
    }
}