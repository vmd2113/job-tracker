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
        // Điều này sẽ kích hoạt validation required nếu cần
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
    // Đảm bảo rằng null/undefined sẽ hiển thị là chuỗi rỗng trong input
    const displayValue = (value === null || value === undefined) ? '' : value;

    // Xác định xem có phải là input loại số hay không
    const isNumberInput = type === 'number';

    return (
        <Input
            {...inputProps}
            ref={ref}
            name={name}
            className={clsName}
            value={displayValue}
            onChange={isNumberInput ? handleNumberChange : onChange}
            onBlur={onBlur}
            label={label}
            error={error?.message}
            required={rules?.required !== undefined}
            type={type}
        />
    );
};

export default FormInput;