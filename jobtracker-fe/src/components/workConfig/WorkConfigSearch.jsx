import React, {useEffect, useMemo, useRef, useState} from 'react';
import useItemManagement from "../../hooks/items/useItemManagement.jsx";
import useWorkTypeManagement from "../../hooks/workTypes/useWorkTypeManagement.jsx";
import InputSelection from "../common/selection/InputSelection.jsx";
import Button from "../common/button/Button.jsx";

const WorkConfigSearch = ({handleSearch, keySearch, loading}) => {
    const searchTimeout = useRef(null);
    const [workConfigs, setWorkConfigs] = useState([]);

    const [error, setError] = useState(null);
    const [workTypes, setWorkTypes] = useState([]);
    const [statuses, setStatuses] = useState([]);
    const [priorities, setPriorities] = useState([]);
    const [formData, setFormData] = useState({
        workTypeName: "",
        priorityId: "",
        oldStatus: "",
        newStatus: "",
        sortBy: 'updateDate',
        sortDirection: 'asc'
    });

    const {getItemByCategoryPriority, getItemByCategoryStatus} = useItemManagement();
    const {fetchAllWorkTypes} = useWorkTypeManagement();

    useEffect(() => {
        const loadWorkTypes = async () => {
            try {

                const response = await fetchAllWorkTypes();

                console.log("WORK CONFIG SEARCH -> FETCH ALL WORK TYPES", response);
                if (response && Array.isArray(response)) {
                    setWorkTypes(response)
                }
            } catch (err) {
                setError(err.message || 'Failed to load work types');
                console.error('Error loading work types:', err);
            }
        };

        loadWorkTypes();
    }, [fetchAllWorkTypes]);

    const listWorkTypes = useMemo(() => {
        return workTypes.map(workType => ({
            value: workType.workTypeName,
            label: `${workType.workTypeName}`
        }));
    }, [workTypes]);


    useEffect(() => {
        const loadStatus = async () => {
            try {
                const response = await getItemByCategoryStatus();
                console.log("WORK CONFIG SEARCH -> FETCH ALL STATUS", response);
                if (response && Array.isArray(response)) {
                    setStatuses(response);
                }
            } catch (err) {
                setError(err.message || 'Failed to load status');
                console.error('Error loading status:', err);
            }
        };

        loadStatus();
    }, [getItemByCategoryStatus]);

    const listStatus = useMemo(() => {
        return statuses.map(status => ({
            value: status.itemId,
            label: `${status.itemName}`
        }));
    }, [statuses]);
    useEffect(() => {
        const loadPriority = async () => {
            try {
                const response = await getItemByCategoryPriority();
                console.log("WORK CONFIG SEARCH -> FETCH ALL PRIORITY", response);
                if (response && Array.isArray(response)) {
                    setPriorities(response);
                }
            } catch (err) {
                setError(err.message || 'Failed to load priority');
                console.error('Error loading priority:', err);
            }
        };

        loadPriority();
    }, [getItemByCategoryPriority]);

    const listPriority = useMemo(() => {
        return priorities.map(priority => ({
            value: priority.itemId,
            label: `${priority.itemName}`
        }));
    }, [priorities]);

    // Sync with external keySearch changes
    useEffect(() => {
        setFormData({
            workTypeName: keySearch.workTypeName || '',
            priorityId: keySearch.priorityId || '',
            oldStatus: keySearch.oldStatus || '',
            newStatus: keySearch.newStatus || '',
            sortBy: keySearch.sortBy || 'updateDate',
            sortDirection: keySearch.sortDirection || 'desc'
        });
    }, [keySearch]);

    const handleWorkTypeChange = (value) => {
        setFormData(prev => ({...prev, workTypeName: value}));
    };
    const handlePriorityChange = (value) => {
        setFormData(prev => ({...prev, priorityId: value}));

    };

    const handleOldStatusChange = (value) => {
        setFormData(prev => ({...prev, oldStatus: value}));
    };

    const handleNewStatusChange = (value) => {
        setFormData(prev => ({...prev, newStatus: value}));
    };

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setFormData(prev => ({...prev, [name]: value}));

        if (searchTimeout.current) {
            clearTimeout(searchTimeout.current);
        }
    };





    const handleSubmit = (e) => {
        e.preventDefault();
        if (searchTimeout.current) {
            clearTimeout(searchTimeout.current);
        }
        handleSearch(formData);
    };

    const handleReset = () => {
        if (searchTimeout.current) {
            clearTimeout(searchTimeout.current);
        }

        const resetData = {
            workTypeName: '',
            priorityId: '',
            oldStatus: '',
            newStatus: '',
            sortBy: 'updateDate',
            sortDirection: 'desc'
        };

        setFormData(resetData);
        handleSearch(resetData)
    };

    console.log("DATA SEARCH FORM: ", formData)


    useEffect(() => {
        return () => {
            if (searchTimeout.current) {
                clearTimeout(searchTimeout.current);
            }
        };
    }, []);




    return (
        <div className="w-full px-4 py-6 sm:px-6 lg:px-8">
            <div className="bg-white rounded-lg shadow-md border border-gray-200">
                <form onSubmit={handleSubmit} className="p-4 sm:p-6 lg:p-8">
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 lg:gap-6">
                        {/* Work Type Name Field */}
                        <div className="w-full">
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Tên loại công việc
                            </label>
                            <InputSelection
                                options={listWorkTypes}
                                value={formData.workTypeName}
                                onChange={handleWorkTypeChange}
                                placeholder="tên loại công việc"
                                disabled={loading}
                                className="w-full"
                            />
                        </div>

                        <div className="w-full">
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Mức độ ưu tiên
                            </label>
                            <InputSelection
                                options={listPriority}
                                value={formData.priorityId}
                                onChange={handlePriorityChange}
                                placeholder="mức độ ưu tiên"
                                disabled={loading}
                                className="w-full"
                            />
                        </div>

                        <div className="w-full">
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Trạng thái cũ
                            </label>
                            <InputSelection
                                options={listStatus}
                                value={formData.oldStatus}
                                onChange={handleOldStatusChange}
                                placeholder="Trạng thái cũ"
                                disabled={loading}
                                className="w-full"
                            />
                        </div>

                        <div className="w-full">
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Trạng thái mới
                            </label>
                            <InputSelection
                                options={listStatus}
                                value={formData.newStatus}
                                onChange={handleNewStatusChange}
                                placeholder="trạng thái mới"
                                disabled={loading}
                                className="w-full"
                            />
                        </div>

                        {/* Buttons Section */}
                        <div className="mt-6 flex flex-col sm:flex-row justify-center gap-3 sm:gap-4">
                            <Button
                                type="search"
                                variant="primary"
                                label="Search"
                                tooltip="Tìm kiếm"
                                size="md"
                                className="w-full sm:w-32"
                                disabled={loading}
                                onClick={handleSubmit}
                            />
                            <Button
                                type="reset"
                                tooltip="Reset"
                                variant="outline"
                                label="Reset"
                                size="md"
                                className="w-full sm:w-32"
                                onClick={handleReset}
                                disabled={loading}
                            />
                        </div>
                    </div>
                </form>
            </div>


        </div>
    );
};

export default WorkConfigSearch;