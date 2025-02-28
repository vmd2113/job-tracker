import React from 'react';
import {useController} from 'react-hook-form';
import Input from './Input';

const FormInput = ({
                       control,
                       name,
                       clsName,
                       rules,
                       label,
                       type,
                       ...inputProps
                   }) => {
    const {
        field: {onChange, onBlur, value, ref},
        fieldState: {error, isTouched}
    } = useController({
        name,
        control,
        rules
    });

    // Xử lý đặc biệt cho trường hợp input number
    const handleNumberChange = (e) => {
        const val = e.target.value;

        // Nếu input rỗng, set giá trị về undefined để React Hook Form hiểu là không có giá trị
        if (val === '') {
            onChange(undefined);
            return;
        }

        // Chuyển đổi chuỗi thành số và kiểm tra tính hợp lệ
        const numValue = parseFloat(val);
        if (!isNaN(numValue)) {
            onChange(numValue); // Lưu giá trị là số, không phải chuỗi
        }
    };

    // Xử lý hiển thị giá trị trong input
    const processDisplayValue = (val) => {
        // Xử lý null/undefined
        if (val === null || val === undefined) {
            return '';
        }

        // Xử lý trường hợp là object
        if (typeof val === 'object') {
            console.warn('FormInput received object value for field:', name, val);
            return '';
        }

        // Trả về giá trị nguyên bản cho các trường hợp còn lại
        return val;
    };

    // Áp dụng xử lý giá trị
    const displayValue = processDisplayValue(value);

    // Xác định xem có phải là input loại số hay không
    const isNumberInput = type === 'number';

    // Xử lý onChange tùy theo loại input
    const handleChange = (e) => {
        if (isNumberInput) {
            handleNumberChange(e);
        } else {
            // Đảm bảo chúng ta đang truyền giá trị đúng
            if (e && e.target) {
                onChange(e.target.value);
            } else {
                onChange(e);
            }
        }
    };

    return (
        <Input
            {...inputProps}
            ref={ref}
            name={name}
            className={clsName}
            value={displayValue}
            onChange={handleChange}
            onBlur={onBlur}
            label={label}
            error={error?.message}
            required={rules?.required !== undefined}
            type={type}
        />
    );
};

export default FormInput;