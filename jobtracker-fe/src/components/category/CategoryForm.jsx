import React, {useState} from 'react';
import {Controller, useForm} from "react-hook-form";
import FormInput from "../common/input/FormInput.jsx";
import InputSelection from "../common/selection/InputSelection.jsx";


const CategoryForm = React.forwardRef((
    {initialData, onSubmit, type},
    ref
) => {

    const {
        control,
        handleSubmit,
        formState: {errors},
        trigger,
        reset
    } = useForm({
        defaultValues: {
            categoryName: initialData?.categoryName || '',
            categoryCode: initialData?.categoryCode || '',
            status: type === 'add' ? 1 : (initialData?.status ?? 1),

        },
        mode: 'onTouched'
    });

    const STATUS_OPTIONS = [
        {value: 1, label: "Hoạt động"},
        {value: 0, label: "Không hoạt động"}
    ];

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);


    console.log("CATEGORY FORM -> CATEGORY FORM");
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
        <div className="relative w-full">
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 md:space-y-6" autoComplete="off">
                {error && (
                    <div className="p-4 mb-4 text-sm text-red-600 bg-red-50 rounded-lg">
                        {error}
                    </div>
                )}
                <div className={`space-y-4`}>
                    <FormInput
                        control={control}
                        name="categoryCode"
                        label="Category Code"
                        type="text"
                        placeholder="Nhập mã danh mục"
                        rules={{
                            required: 'Category Code là bắt buộc',
                            minLength: {
                                value: 3,
                                message: 'Category Code phải có ít nhất 6 ký tự'
                            },
                            maxLength: {
                                value: 100,
                                message: 'Category Code không được vượt quá 100 ký tự'
                            },
                            pattern: {
                                value: /^[a-zA-Z0-9_]+$/,
                                message: 'Category chỉ được chứa chữ cái, số và dấu gạch dưới'

                            }
                        }}
                    >

                    </FormInput>

                    <FormInput
                        control={control}
                        name="categoryName"
                        label="Category Name"

                        type="text"
                        placeholder="Nhập tên danh mục"
                        rules={{
                            required: 'Category Name là bắt buộc',
                            minLength: {
                                value: 3,
                                message: 'Category Name phải có ít nhất 6 ký tự'
                            },
                            maxLength: {
                                value: 100,
                                message: 'Category Name không được vượt quá 100 ký tự'
                            },

                        }}
                    >

                    </FormInput>

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
                </div>
            </form>
        </div>
    );
});


export default CategoryForm;