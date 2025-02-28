import React, {useEffect, useMemo, useState} from 'react';
import {Controller, useForm} from "react-hook-form";
import useWorkTypeManagement from "../../hooks/workTypes/useWorkTypeManagement.jsx";
import InputSelection from "../common/selection/InputSelection.jsx";
import useItemManagement from "../../hooks/items/useItemManagement.jsx";
import DateTimePicker from "../common/datepicker/DateTimePicker.jsx";
import FileUpload from "../common/uploadfile/FileUpload.jsx";
import FormInput from "../common/input/FormInput.jsx";

const WorkForm = React.forwardRef(({initialData, onSubmit, type}, ref) => {
    const {
        control,
        handleSubmit,
        formState: {errors},
        trigger,
        watch,
        reset
    } = useForm({
        defaultValues: {
            workCode: initialData?.workCode || '',
            workContent: initialData?.workContent || '',
            workTypeId: initialData?.workTypeId || null,
            priorityId: type === 'add' ? 1 : initialData?.priorityId || null,
            startTime: initialData?.startTime || '',
            endTime: initialData?.endTime || '',
            assignedUserId: initialData?.assignedUserId || null,
            status: type === 'add' ? 1 : (initialData?.status ?? 1),
        },
        mode: 'onTouched'
    });

    const STATUS_OPTIONS = [
        {value: 1, label: "Hoạt động"},
        {value: 0, label: "Không hoạt động"}
    ];

    const {fetchAllWorkTypes} = useWorkTypeManagement();
    const {getItemByCategoryPriority} = useItemManagement();

    const [workTypes, setWorkTypes] = useState([]);
    const [itemByCategoryPriority, setItemByCategoryPriority] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const startTime = watch('startTime');
    const endTime = watch('endTime');

    // Load work types
    useEffect(() => {
        const loadWorkTypes = async () => {
            try {
                const response = await fetchAllWorkTypes();
                if (response && Array.isArray(response)) {
                    setWorkTypes(response);
                }
            } catch (err) {
                console.error('Error loading work types:', err);
                setError('Không thể tải loại công việc');
            }
        };
        loadWorkTypes();
    }, [fetchAllWorkTypes]);

    const listWorkTypes = useMemo(() => {
        return workTypes.map(workType => ({
            value: workType.workTypeId,
            label: workType.workTypeName
        }));
    }, [workTypes]);

    useEffect(() => {
        const loadPriorities = async () => {
            try {
                const response = await getItemByCategoryPriority();
                if (response && Array.isArray(response)) {
                    setItemByCategoryPriority(response);
                }
            } catch (err) {
                console.error('Error loading priorities:', err);
                setError('Không thể tải mức độ ưu tiên');
            }
        };
        loadPriorities();
    }, [getItemByCategoryPriority]);

    const listItemByCategoryPriority = useMemo(() => {
        return itemByCategoryPriority.map(item => ({
            value: item.itemId,
            label: item.itemName
        }));
    }, [itemByCategoryPriority]);

    useEffect(() => {
        if (initialData) {
            reset({
                ...initialData,
                startTime: initialData.startTime || null,
                endTime: initialData.endTime || null
            });
        }
    }, [initialData, reset]);

    // Format the data to be compatible with the API
    const formatSubmitData = (data) => {
        // Convert form data to the format expected by the API
        return {
            workCode: data.workCode,
            workContent: data.workContent,
            workTypeId: data.workTypeId,
            priorityId: data.priorityId,
            startTime: data.startTime, // Ensure this is a valid ISO string
            endTime: data.endTime, // Ensure this is a valid ISO string
            assignedUserId: data.assignedUserId,
            status: data.status
        };
    };

    // Custom submit handler to format data before passing to onSubmit
    const handleFormSubmit = (data) => {
        const formattedData = formatSubmitData(data);
        onSubmit(formattedData);
    };

    React.useImperativeHandle(ref, () => ({
        submit: () => {
            trigger().then(isValid => {
                if (isValid) {
                    handleSubmit(handleFormSubmit)();
                }
            });
        }
    }));

    const formContainerStyle = {
        maxHeight: '80vh',
        overflowY: 'auto',
        position: 'relative',
        padding: '1rem'
    };

    return (
        <div style={formContainerStyle}>
            <form onSubmit={handleSubmit(handleFormSubmit)} className="space-y-4 md:space-y-6" autoComplete="off">
                <div className="space-y-4">
                    <FormInput
                        control={control}
                        name="workCode"
                        label="Mã công việc"
                        disabled={type === 'add'} // Code will be generated by backend
                        type="text"
                        placeholder="Nhập mã công việc"
                        className="w-full"
                        labelClassName="text-sm font-medium text-gray-700"
                        inputClassName="w-full rounded-md"
                    />
                </div>

                <div className="space-y-4">
                    <FormInput
                        control={control}
                        name="workContent"
                        label="Nội dung công việc"
                        type="text"
                        placeholder="Nội dung công việc"
                        rules={{
                            required: 'Nội dung công việc là bắt buộc',
                            minLength: {
                                value: 6,
                                message: 'Nội dung công việc phải có ít nhất 6 ký tự'
                            },
                            maxLength: {
                                value: 500,
                                message: 'Nội dung công việc không được vượt quá 500 ký tự'
                            },
                        }}
                        className="w-full"
                        labelClassName="text-sm font-medium text-gray-700"
                        inputClassName="w-full rounded-md"
                    />
                </div>

                <div className="space-y-4">
                    <label className="block text-sm font-medium text-gray-700">
                        Loại công việc
                    </label>
                    <Controller
                        name="workTypeId"
                        control={control}
                        rules={{required: "Loại công việc là bắt buộc"}}
                        render={({field}) => (
                            <InputSelection
                                options={listWorkTypes}
                                value={field.value}
                                onChange={field.onChange}
                                placeholder="Chọn loại công việc"
                                className="w-full"
                                menuPortalTarget={document.body}
                                styles={{
                                    menuPortal: base => ({...base, zIndex: 9999}),
                                    menu: base => ({...base, zIndex: 9999})
                                }}
                            />
                        )}
                    />
                    {errors.workTypeId && (
                        <p className="mt-1 text-sm text-red-600">
                            {errors.workTypeId.message}
                        </p>
                    )}
                </div>

                {/* Priority Selection */}
                <div className="space-y-4">
                    <label className="block text-sm font-medium text-gray-700">
                        Mức độ ưu tiên
                    </label>
                    <Controller
                        name="priorityId"
                        control={control}
                        rules={{required: "Mức độ ưu tiên là bắt buộc"}}
                        render={({field}) => (
                            <InputSelection
                                options={listItemByCategoryPriority}
                                value={field.value}
                                onChange={field.onChange}
                                placeholder="Chọn mức độ ưu tiên"
                                className="w-full"
                                menuPortalTarget={document.body}
                                styles={{
                                    menuPortal: base => ({...base, zIndex: 9999}),
                                    menu: base => ({...base, zIndex: 9999})
                                }}
                            />
                        )}
                    />
                    {errors.priorityId && (
                        <p className="mt-1 text-sm text-red-600">
                            {errors.priorityId.message}
                        </p>
                    )}
                </div>

                <div className="space-y-4">
                    <Controller
                        name="startTime"
                        control={control}
                        rules={{
                            required: "Thời gian bắt đầu là bắt buộc",
                            validate: value => {
                                if (new Date(value) < new Date()) {
                                    return "Thời gian bắt đầu không thể là quá khứ";
                                }
                                return true;
                            }
                        }}
                        render={({field}) => (
                            <DateTimePicker
                                {...field}
                                label="Thời gian bắt đầu"
                                placeholder="Chọn thời gian bắt đầu"
                                error={errors.startTime?.message}
                                required
                                minDate={new Date()}
                            />
                        )}
                    />
                </div>

                <div className="space-y-4">
                    <Controller
                        name="endTime"
                        control={control}
                        disabled={type === 'add'} // End time will be calculated by backend
                        rules={{
                            validate: value => {
                                if (value && startTime && new Date(value) <= new Date(startTime)) {
                                    return "Thời gian kết thúc phải sau thời gian bắt đầu";
                                }
                                return true;
                            }
                        }}
                        render={({field}) => (
                            <DateTimePicker
                                {...field}
                                label="Thời gian kết thúc"
                                placeholder="Chọn thời gian kết thúc"
                                error={errors.endTime?.message}
                                minDate={startTime ? new Date(startTime) : new Date()}
                            />
                        )}
                    />
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
                                    disabled={type === 'add'} // Status is fixed for new items
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

                <div className="space-y-4">
                    <Controller
                        name="attachments"
                        control={control}
                        render={({field}) => (
                            <FileUpload
                                {...field}
                                multiple
                                label="File đính kèm"
                                error={errors.attachments?.message}
                            />
                        )}
                    />
                </div>
            </form>
        </div>
    );
});

export default WorkForm;