import React, {useEffect, useMemo, useState} from 'react';
import {Controller, useForm} from "react-hook-form";
import InputSelection from "../common/selection/InputSelection.jsx";
import useWorkTypeManagement from "../../hooks/workTypes/useWorkTypeManagement.jsx";
import useItemManagement from "../../hooks/items/useItemManagement.jsx";

const WorkConfigForm = React.forwardRef(({initialData, onSubmit, type}, ref) => {
    const {
        control,
        handleSubmit,
        formState: {errors},
        trigger,
        reset
    } = useForm({
        defaultValues: {
            workTypeId: initialData?.workTypeId || null,
            priorityId: initialData?.priorityId || null,
            oldStatusId: initialData?.oldStatusId || null,
            newStatusId: initialData?.newStatusId || null,
        },
        mode: 'onTouched'
    });

    const {fetchAllWorkTypes} = useWorkTypeManagement();
    const {getItemByCategoryPriority, getItemByCategoryStatus} = useItemManagement();

    const [workTypes, setWorkTypes] = useState([]);
    const [itemByCategoryPriority, setItemByCategoryPriority] = useState([]);
    const [itemByCategoryStatus, setItemByCategoryStatus] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

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
            }
        };
        loadWorkTypes();
    }, [fetchAllWorkTypes]);

    // Load priorities
    useEffect(() => {
        const loadPriorities = async () => {
            try {
                const response = await getItemByCategoryPriority();
                if (response && Array.isArray(response)) {
                    setItemByCategoryPriority(response);
                }
            } catch (err) {
                console.error('Error loading priorities:', err);
            }
        };
        loadPriorities();
    }, [getItemByCategoryPriority]);

    // Load status items
    useEffect(() => {
        const loadStatus = async () => {
            try {
                const response = await getItemByCategoryStatus();
                if (response && Array.isArray(response)) {
                    setItemByCategoryStatus(response);
                }
            } catch (err) {
                console.error('Error loading status:', err);
            }
        };
        loadStatus();
    }, [getItemByCategoryStatus]);

    // Transform options for workTypes (keep as is since it's working)
    const listWorkTypes = useMemo(() => {
        return workTypes.map(workType => ({
            value: workType.workTypeId,
            label: workType.workTypeName
        }));
    }, [workTypes]);

    // Transform options for priorities
    const listItemByCategoryPriority = useMemo(() => {
        return itemByCategoryPriority.map(item => ({
            value: item.itemId,
            label: item.itemName
        }));
    }, [itemByCategoryPriority]);

    // Transform options for status - modify the value to match oldStatusId/newStatusId
    const listItemByCategoryStatus = useMemo(() => {
        return itemByCategoryStatus.map(item => ({
            value: item.itemId, // This should match with oldStatusId/newStatusId
            label: item.itemName
        }));
    }, [itemByCategoryStatus]);

    React.useImperativeHandle(ref, () => ({
        submit: () => {
            trigger().then(isValid => {
                if (isValid) {
                    handleSubmit(onSubmit)();
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
        <div style ={formContainerStyle}>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 md:space-y-6" autoComplete="off">
                {/* Work Type Selection */}
                <div className="space-y-4">
                    <label className="block text-sm font-medium text-gray-700">
                        Loại công việc
                    </label>
                    <Controller
                        name="workTypeId"
                        control={control}
                        rules={{ required: "Loại công việc là bắt buộc" }}
                        render={({field}) => (
                            <InputSelection
                                options={listWorkTypes}
                                value={field.value}
                                onChange={field.onChange}
                                placeholder="Chọn loại công việc"
                                className="w-full"
                                menuPortalTarget={document.body}
                                styles={{
                                    menuPortal: base => ({ ...base, zIndex: 9999 }),
                                    menu: base => ({ ...base, zIndex: 9999 })
                                }}
                            />
                        )}
                    />
                </div>

                {/* Priority Selection */}
                <div className="space-y-4">
                    <label className="block text-sm font-medium text-gray-700">
                        Mức độ ưu tiên
                    </label>
                    <Controller
                        name="priorityId"
                        control={control}
                        rules={{ required: "Mức độ ưu tiên là bắt buộc" }}
                        render={({field}) => (
                            <InputSelection
                                options={listItemByCategoryPriority}
                                value={field.value}
                                onChange={field.onChange}
                                placeholder="Chọn mức độ ưu tiên"
                                className="w-full"
                                menuPortalTarget={document.body}
                                styles={{
                                    menuPortal: base => ({ ...base, zIndex: 9999 }),
                                    menu: base => ({ ...base, zIndex: 9999 })
                                }}
                            />
                        )}
                    />
                </div>

                {/* Old Status Selection */}
                <div className="space-y-4">
                    <label className="block text-sm font-medium text-gray-700">
                        Trạng thái cũ
                    </label>
                    <Controller
                        name="oldStatusId"
                        control={control}
                        rules={{ required: "Trạng thái cũ là bắt buộc" }}
                        render={({field}) => (
                            <InputSelection
                                options={listItemByCategoryStatus}
                                value={field.value}
                                onChange={field.onChange}
                                placeholder="Chọn trạng thái cũ"
                                className="w-full"
                                menuPortalTarget={document.body}
                                styles={{
                                    menuPortal: base => ({ ...base, zIndex: 9999 }),
                                    menu: base => ({ ...base, zIndex: 9999 })
                                }}
                            />
                        )}
                    />
                </div>

                {/* New Status Selection */}
                <div className="space-y-4">
                    <label className="block text-sm font-medium text-gray-700">
                        Trạng thái mới
                    </label>
                    <Controller
                        name="newStatusId"
                        control={control}
                        rules={{ required: "Trạng thái mới là bắt buộc" }}
                        render={({field}) => (
                            <InputSelection
                                options={listItemByCategoryStatus}
                                value={field.value}
                                onChange={field.onChange}
                                placeholder="Chọn trạng thái mới"
                                className="w-full"
                                menuPortalTarget={document.body}
                                styles={{
                                    menuPortal: base => ({ ...base, zIndex: 9999 }),
                                    menu: base => ({ ...base, zIndex: 9999 })
                                }}
                            />
                        )}
                    />
                </div>
            </form>
        </div>
    );
});

export default WorkConfigForm;