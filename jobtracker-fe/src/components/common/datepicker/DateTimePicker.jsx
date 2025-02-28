import React from 'react';
import DatePicker from 'react-datepicker';
import {Calendar} from 'lucide-react';
import "react-datepicker/dist/react-datepicker.css";

const DateTimePicker = ({
                            value,
                            onChange,
                            format = 'yyyy-MM-dd\'T\'HH:mm:ss',
                            placeholder = 'Chọn thời gian',
                            disabled = false,
                            error,
                            className = '',
                            required = false,
                            label,
                            minDate
                        }) => {

    // Hàm tùy chỉnh để định dạng ngày thành chuỗi theo yêu cầu của backend
    const formatDateToBackendString = (date) => {
        if (!date) return null;

        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');

        // Tạo chuỗi định dạng chính xác cho backend: yyyy-MM-dd'T'HH:mm:ss
        return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
    };

    // Hàm xử lý sự kiện onChange mới
    const handleDateChange = (date) => {
        if (!date) {
            onChange(null);
            return;
        }

        // Gọi hàm onChange với chuỗi định dạng đúng theo yêu cầu backend
        onChange(formatDateToBackendString(date));
    };

    return (
        <div className={`space-y-2 ${className}`}>
            {label && (
                <label className="block text-sm font-medium text-gray-700">
                    {label}
                    {required && <span className="text-red-500 ml-1">*</span>}
                </label>
            )}
            <div className="relative">
                <DatePicker
                    selected={value ? new Date(value) : null}
                    onChange={handleDateChange}
                    showTimeSelect
                    timeFormat="HH:mm"
                    timeIntervals={15}
                    dateFormat="dd/MM/yyyy HH:mm"
                    placeholderText={placeholder}
                    disabled={disabled}
                    minDate={minDate}
                    className={`
                        w-full
                        pl-10
                        pr-4
                        py-2
                        border
                        rounded-md
                        focus:outline-none
                        focus:ring-2
                        focus:ring-blue-500
                        ${error ? 'border-red-500' : 'border-gray-300'}
                        ${disabled ? 'bg-gray-100 cursor-not-allowed' : 'bg-white'}
                    `}
                />
                <Calendar className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400"/>
            </div>
            {error && (
                <p className="mt-1 text-sm text-red-600">{error}</p>
            )}
        </div>
    );
};

export default DateTimePicker;