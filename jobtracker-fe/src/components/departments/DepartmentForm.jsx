import React, {useMemo} from 'react';
import {Controller, useForm} from "react-hook-form";
import useDepartmentManagement from "../../hooks/departments/useDepartmentManagement.jsx";
import FormInput from "../common/input/FormInput.jsx";
import InputSelection from "../common/selection/InputSelection.jsx";


const DepartmentForm = React.forwardRef(({initialData, onSubmit, type}, ref) => {
    const {
        control,
        handleSubmit,
        formState: {errors},
        trigger,
        setValue
    } = useForm({
        defaultValues: initialData || {
            departmentName: initialData?.departmentName || '',
            departmentCode: initialData?.departmentCode || '',
            departmentParentId: initialData?.departmentParentId || null,
            status: type === 'add' ? 1 : (initialData?.status ?? 1),

        },
        mode: 'onTouched'

    })

    const STATUS_OPTIONS = [
        {value: 1, label: "Hoạt động"},
        {value: 0, label: "Không hoạt động"}
    ];

    const {allDepartments, loading, error} = useDepartmentManagement();

    const listDepartment = useMemo(() => {
        return allDepartments.map(dep => ({
            value: dep.departmentId,
            label: dep.departmentName,
        }))
    }, [allDepartments])

    const handleDepartmentParentChange = (selectedOption) => {
        // Cập nhật giá trị cho departmentParentId
        setValue('departmentParentId', selectedOption?.value || null);
    };


    React.useImperativeHandle(ref, () => ({
        submit: () => {
            trigger().then(isValid => {
                if (isValid) {
                    handleSubmit(onSubmit)();
                }
            });
        }
    }))

    return (
        <div className="relative w-full">
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 md:space-y-6" autoComplete="off">
                {error && (
                    <div className="p-4 mb-4 text-sm text-red-600 bg-red-50 rounded-lg">
                        {error}
                    </div>
                )}

                <div className="space-y-4">

                    <FormInput
                        control={control}
                        name="departmentName"
                        label="Tên phoòng ban"
                        type="text"
                        placeholder="Nhập tên phòng ban"
                        rules={{
                            required: 'Tên phòng ban là bắt buộc',
                            minLength: {
                                value: 6,
                                message: 'Tên phòng ban phải có ít nhất 6 ký tự'
                            },
                            maxLength: {
                                value: 500,
                                message: 'Tên phòng ban không được vượt quá 500 ký tự'
                            }
                        }}
                        className="w-full"
                        labelClassName="text-sm font-medium text-gray-700"
                        inputClassName="w-full mt-1"
                    />
                </div>

                <div className="space-y-4">

                    <FormInput
                        control={control}
                        name="departmentCode"
                        label="Mã phoòng ban"
                        type="text"
                        placeholder="Nhập tên phòng ban"
                        rules={{
                            required: 'Mã phòng ban là bắt buộc',
                            minLength: {
                                value: 6,
                                message: 'Mã phòng ban phải có ít nhất 6 ký tự'
                            },
                            maxLength: {
                                value: 100,
                                message: 'Mã phòng ban không được vượt quá 100 ký tự'
                            }
                        }}
                        className="w-full"
                        labelClassName="text-sm font-medium text-gray-700"
                        inputClassName="w-full mt-1"
                    />

                </div>

                <label className="block text-sm font-medium text-gray-700">
                    Chọn phòng ban cha
                </label>
                <div className="relative">
                    <Controller
                        name="departmentParentId"  // Sửa name thành departmentParentId
                        control={control}
                        render={({field}) => (
                            <InputSelection
                                options={listDepartment}
                                value={field.value}  // Just pass the value directly
                                onChange={(value) => {
                                    field.onChange(value);
                                }}
                                placeholder="Chọn phòng ban cha"
                                className="w-full"
                                menuPortalTarget={document.body}
                                styles={{
                                    menuPortal: (base) => ({
                                        ...base,
                                        zIndex: 9999
                                    }),
                                    menu: (base) => ({
                                        ...base,
                                        zIndex: 9999
                                    })
                                }}
                            />
                        )}
                    />
                    {errors.departmentParentId && (
                        <p className="mt-1 text-sm text-red-600">
                            {errors.departmentParentId.message}
                        </p>
                    )}

                </div>

                <label className="block text-sm font-medium text-gray-700">
                    Trạng thái
                </label>
                <div className="relative">
                    <Controller
                        name="status"
                        control={control}
                        rules={{
                            required: "Trạng thái là bắt buộc"
                        }}
                        render={({field}) => (
                            <InputSelection
                                options={STATUS_OPTIONS}
                                value={field.value}
                                onChange={field.onChange}
                                placeholder="Chọn trạng thái"
                                className="w-full"
                                disabled={type === 'add'}
                                menuPortalTarget={document.body}
                                styles={{
                                    menuPortal: (base) => ({
                                        ...base,
                                        zIndex: 9999
                                    }),
                                    menu: (base) => ({
                                        ...base,
                                        zIndex: 9999
                                    })
                                }}
                            />
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
export default DepartmentForm;