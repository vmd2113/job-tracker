import React from 'react';
import {Controller, useForm} from 'react-hook-form';
import FormInput from "../common/input/FormInput.jsx";


const WorkTypeForm = React.forwardRef(({initialData, onSubmit, type}, ref) => {
    const {
        control,
        handleSubmit,
        formState: {errors},
        trigger,
    } = useForm({
        defaultValues: {
            workTypeName: initialData?.workTypeName || '',
            workTypeCode: initialData?.workTypeCode || '',
            processTime: initialData?.processTime || '',
            status: type === 'add' ? 1 : (initialData?.status ?? 1),
        },
        mode: 'onTouched'
    });

    const STATUS_OPTIONS = [
        {value: 1, label: "Hoạt động"},
        {value: 0, label: "Không hoạt động"}
    ];

    // Validate process time format
    const validateProcessTime = (value) => {
        // Check if it's a valid number
        if (isNaN(value)) return "Thời gian xử lý phải là số";

        const parts = value.toString().split('.');
        const integerPart = parts[0];
        const decimalPart = parts[1] || '';

        // Check length constraints
        if (integerPart.length > 10) {
            return "Phần nguyên không được vượt quá 10 chữ số";
        }
        if (decimalPart.length > 2) {
            return "Phần thập phân không được vượt quá 2 chữ số";
        }

        return true;
    };

    React.useImperativeHandle(ref, () => ({
        submit: () => {
            trigger().then(isValid => {
                if (isValid) {
                    handleSubmit(onSubmit)();
                }
            });
        }
    }));

    return (
        <div className="w-full">
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6" autoComplete="off">
                <FormInput
                    control={control}
                    name="workTypeCode"
                    label="Work Type Code"
                    placeholder="Nhập code loại công việc"
                    rules={{
                        required: 'Work Type Code là bắt buộc',
                        minLength: {
                            value: 6,
                            message: 'Work Type Code phải có ít nhất 6 ký tự'
                        },
                        maxLength: {
                            value: 100,
                            message: 'Work Type Code không được vượt quá 100 ký tự'
                        }
                    }}
                />

                <FormInput
                    control={control}
                    name="workTypeName"
                    label="Work Type Name"
                    placeholder="Nhập tên loại công việc"
                    rules={{
                        required: 'Work Type Name là bắt buộc',
                        minLength: {
                            value: 6,
                            message: 'Work Type Name phải có ít nhất 6 ký tự'
                        },
                        maxLength: {
                            value: 500,
                            message: 'Work Type Name không được vượt quá 500 ký tự'
                        }
                    }}
                />

                <FormInput
                    control={control}
                    name="processTime"
                    label="Process Time"
                    placeholder="Nhập Thời gian xử lý"
                    rules={{
                        required: 'Thời gian xử lý là bắt buộc',
                        validate: validateProcessTime
                    }}
                    type="number"
                    step="0.01"
                />

                <div className="space-y-2">
                    <label className="block text-sm font-medium">Trạng thái</label>
                    <Controller
                        name="status"
                        control={control}
                        rules={{required: "Trạng thái là bắt buộc"}}
                        render={({field}) => (
                            <select
                                {...field}
                                className="w-full rounded-md border border-gray-300 p-2"
                                disabled={type === 'add'}
                            >
                                {STATUS_OPTIONS.map(option => (
                                    <option key={option.value} value={option.value}>
                                        {option.label}
                                    </option>
                                ))}
                            </select>
                        )}
                    />
                    {errors.status && (
                        <p className="mt-1 text-sm text-red-600">
                            {errors.status.message}
                        </p>
                    )}
                </div>
            </form>
        </div>
    );
});

export default WorkTypeForm;