import React, {useEffect, useState} from 'react';
import {useForm, Controller} from 'react-hook-form';
import FormInput from "../common/input/FormInput.jsx";
import InputSelection from "../common/selection/InputSelection.jsx";
import useDepartmentManagement from "../../hooks/departments/useDepartmentManagement.jsx";

const UserForm = React.forwardRef(({initialData, onSubmit, type}, ref) => {
    const {
        control,
        handleSubmit,
        formState: {errors},
        trigger,
        reset
    } = useForm({
        defaultValues: {
            username: initialData?.username || '',
            firstName: initialData?.firstName || '',
            lastName: initialData?.lastName || '',
            email: initialData?.email || '',
            password: '',
            phoneNumber: initialData?.phoneNumber || '',
            status: type === 'add' ? 1 : (initialData?.status ?? 1),
            departmentId: initialData?.departmentId || null
        },
        mode: 'onTouched'
    });

    const STATUS_OPTIONS = [
        {value: 1, label: "Hoạt động"},
        {value: 0, label: "Không hoạt động"}
    ];

    const {fetchDepartmentWithHierarchy} = useDepartmentManagement();
    const [departments, setDepartments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const flattenDepartments = React.useMemo(() => {
        const flatten = (items, prefix = '', level = 0) => {
            return items.reduce((acc, item) => {
                const indent = '\u00A0\u00A0'.repeat(level);
                acc.push({
                    value: item.departmentId,
                    label: `${indent}${item.departmentName}`,
                    level
                });

                if (item.children && item.children.length > 0) {
                    acc.push(...flatten(item.children, prefix, level + 1));
                }

                return acc;
            }, []);
        };

        return departments.length > 0 ? flatten(departments) : [];
    }, [departments]);

    useEffect(() => {
        const loadDepartments = async () => {
            try {
                setLoading(true);
                setError(null);
                const response = await fetchDepartmentWithHierarchy();
                if (response?.data && Array.isArray(response.data)) {
                    setDepartments(response.data);
                }
            } catch (err) {
                setError(err.message || 'Failed to load departments');
                console.error('Error loading departments:', err);
            } finally {
                setLoading(false);
            }
        };

        loadDepartments();
    }, [fetchDepartmentWithHierarchy]);


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

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
                    {/* First Column */}
                    <div className="space-y-4">
                        <FormInput
                            control={control}
                            name="username"
                            label="Username"

                            type="text"
                            placeholder="Nhập tên tài khoản"
                            rules={{
                                required: 'Username là bắt buộc',
                                minLength: {
                                    value: 6,
                                    message: 'Username phải có ít nhất 6 ký tự'
                                },
                                maxLength: {
                                    value: 500,
                                    message: 'Username không được vượt quá 500 ký tự'
                                },
                                pattern: {
                                    value: /^[a-zA-Z0-9_]+$/,
                                    message: 'Username chỉ được chứa chữ cái, số và dấu gạch dưới'

                                }
                            }}
                            className="w-full"
                            labelClassName="text-sm font-medium text-gray-700"
                            inputClassName="w-full rounded-md"
                        />

                        <FormInput
                            control={control}
                            name="lastName"
                            label="Họ"
                            type="text"
                            placeholder="Nhập họ"
                            rules={{
                                required: 'Họ là bắt buộc',
                                maxLength: {
                                    value: 500,
                                    message: 'Họ không được vượt quá 500 ký tự'
                                }
                            }}
                            className="w-full"
                            labelClassName="text-sm font-medium text-gray-700"
                            inputClassName="w-full rounded-md"
                        />

                        <FormInput
                            control={control}
                            name="firstName"
                            label="Tên"
                            type="text"
                            placeholder="Nhập tên"
                            rules={{
                                required: 'Tên là bắt buộc'
                            }}
                            className="w-full"
                            labelClassName="text-sm font-medium text-gray-700"
                            inputClassName="w-full rounded-md"
                        />

                        <FormInput
                            control={control}
                            name="email"
                            label="Email"
                            type="email"
                            placeholder="Nhập email"
                            rules={{
                                required: 'Email là bắt buộc',
                                pattern: {
                                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                    message: 'Email không hợp lệ'
                                },
                                maxLength: {
                                    value: 500,
                                    message: 'Email không được vượt quá 500 ký tự'
                                }
                            }}
                            className="w-full"
                            labelClassName="text-sm font-medium text-gray-700"
                            inputClassName="w-full rounded-md"
                        />
                    </div>

                    {/* Second Column */}
                    <div className="space-y-4">
                        {(type === 'add' || !initialData) && (
                            <FormInput
                                control={control}
                                name="password"
                                label="Password"
                                type="password"
                                placeholder="Nhập password"
                                rules={{
                                    required: 'Password là bắt buộc',
                                    minLength: {
                                        value: 8,
                                        message: 'Password phải có ít nhất 8 ký tự'
                                    }
                                }}
                                className="w-full"
                                labelClassName="text-sm font-medium text-gray-700"
                                inputClassName="w-full rounded-md"
                            />
                        )}

                        <FormInput
                            control={control}
                            name="phoneNumber"
                            label="Số điện thoại"
                            type="text"
                            placeholder="Nhập số điện thoại"
                            rules={{
                                required: 'Số điện thoại là bắt buộc',
                                pattern: {
                                    value: /^[0-9]/,
                                    message: 'Số điện thoại phải có 10 chữ số'
                                },
                                maxLength: {
                                    value: 11,
                                    message: 'Số điện thoại không được vượt quá 11 chữ số'
                                }
                            }}
                            className="w-full"
                            labelClassName="text-sm font-medium text-gray-700"
                            inputClassName="w-full rounded-md"
                        />

                        <div className="space-y-2">
                            <label className="block text-sm font-medium text-gray-700">
                                Phòng ban
                            </label>
                            <div className="relative">
                                <Controller
                                    name="departmentId"
                                    control={control}
                                    render={({field}) => (
                                        <InputSelection
                                            options={flattenDepartments}
                                            value={field.value}
                                            onChange={field.onChange}
                                            placeholder="Chọn phòng ban"
                                            isLoading={loading}
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
                            </div>
                        </div>

                        <div className="space-y-2">
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
                    </div>
                </div>
            </form>
        </div>
    );
});

export default UserForm;