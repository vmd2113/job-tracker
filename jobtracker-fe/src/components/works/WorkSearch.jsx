import React, {useEffect, useMemo, useRef, useState} from 'react';
import useItemManagement from "../../hooks/items/useItemManagement.jsx";
import useWorkTypeManagement from "../../hooks/workTypes/useWorkTypeManagement.jsx";
import useUserManagement from "../../hooks/users/useUserManagement.jsx";
import InputSelection from "../common/selection/InputSelection.jsx";
import Button from "../common/button/Button.jsx";
import Input from "../common/input/Input.jsx";
import DateTimePicker from "../common/datepicker/DateTimePicker.jsx";

const WorkSearch = ({handleSearch, keySearch, loading}) => {
    const searchTimeout = useRef(null);
    const [works, setWorks] = useState([]);
    const [workTypes, setWorkTypes] = useState([]);
    const [statuses, setStatuses] = useState([]);
    const [priorities, setPriorities] = useState([]);
    const [users, setUsers] = useState([]);

    const DEPARTMENT_ID_CURRENT = 6;

    const [formData, setFormData] = useState({
        workCode: ' ',
        workContent: ' ',
        workTypeId: '',
        priorityId: '',
        status: '',
        startTime: '',
        endTime: '',
        assignedUserId: '',
        sortBy: 'updateDate',
        sortDirection: 'desc'

    });

    const {getItemByCategoryPriority, getItemByCategoryStatus} = useItemManagement();
    const {fetchAllWorkTypes} = useWorkTypeManagement();
    const {fetchAllUsers} = useUserManagement();

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


    const listWorkTypes = useMemo(() => {
        return workTypes.map(workType => ({
            value: workType.workTypeId,
            label: `${workType.workTypeName}`
        }));
    }, [workTypes]);

    const listStatus = useMemo(() => {
        return statuses.map(status => ({
            value: status.itemId,
            label: `${status.itemName}`
        }));
    }, [statuses]);


    const listPriority = useMemo(() => {
        return priorities.map(priority => ({
            value: priority.itemId,
            label: `${priority.itemName}`
        }));
    }, [priorities]);


    useEffect(() => {


        const loadUsers = async () => {
            try {
                const response = await fetchAllUsers();
                console.log("WORK CONFIG SEARCH -> FETCH ALL USERS", response);
                if (response && Array.isArray(response)) {
                    setUsers(response);
                }
            } catch (err) {
                setError(err.message || 'Failed to load users');
                console.error('Error loading users:', err);
            }
        };

        loadUsers();
    }, [fetchAllUsers]);


    // get user and filter by department id
    const listUser = useMemo(() => {
        return users
            .filter(user => user.departmentId === DEPARTMENT_ID_CURRENT)
            .map(user => ({
                value: user.userId,
                label: `${user.firstName} ${user.lastName}`
            }));

    }, [users]);


    useEffect(() => {
        setFormData({
            workCode: keySearch.workCode || "",
            workContent: keySearch.workContent || "",
            workTypeId: keySearch.workTypeId || "",
            priorityId: keySearch.priorityId || "",
            status: keySearch.status || "",
            startTime: keySearch.startTime || "",
            endTime: keySearch.endTime || "",
            assignedUserId: keySearch.assignedUserId || "",
            sortBy: keySearch.sortBy || 'updateDate',
            sortDirection: keySearch.sortDirection || 'desc'
        })
    }, [keySearch]);

    const handleWorkChange = (value) => {
        setFormData(prev => ({
            ...prev,
            workTypeId: value
        }));
    }

    const handlePriorityChange = (value) => {
        setFormData(prev => ({
            ...prev,
            priorityId: value
        }));
    }

    const handleStatusChange = (value) => {
        setFormData(prev => ({
            ...prev,
            oldStatusId: value
        }));
    }

    const handleWorkCodeChange = (value) => {
        setFormData(prev => ({
            ...prev,
            workCode: value
        }));
    }

    const handleWorkContentChange = (value) => {
        setFormData(prev => ({
            ...prev,
            workContent: value
        }));
    }

    const handleStartDateChange = (date) => {
        setFormData(prev => ({
            ...prev,
            startTime: date
        }));
    };

    const handleEndDateChange = (date) => {
        setFormData(prev => ({
            ...prev,
            endTime: date
        }));
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
            workCode: ' ',
            workContent: ' ',
            workTypeId: '',
            priorityId: '',
            status: '',
            startTime: '',
            endTime: '',
            assignedUserId: '',
            sortBy: 'updateDate',
            sortDirection: 'desc'
        };

        setFormData(resetData);
        handleSearch(resetData)
    };

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
                    <div className="w-full">
                        <Input
                            type="text"
                            name="workCode"
                            label="Work Code"
                            placeholder="Nhập mã công việc"
                            value={formData.workCode}
                            onChange={handleWorkCodeChange}
                            disabled={loading}
                            className="w-full"
                            labelClassName="text-sm font-medium text-gray-700"
                            inputClassName="w-full mt-1"
                        />
                    </div>

                    {/* Work Type Code Field */}
                    <div className="w-full">
                        <Input
                            type="text"
                            name="workContent"
                            label="Work Type Code"
                            placeholder="Nhập code loại công việc"
                            value={formData.workContent}
                            onChange={handleWorkContentChange}
                            disabled={loading}
                            className="w-full"
                            labelClassName="text-sm font-medium text-gray-700"
                            inputClassName="w-full mt-1"
                        />

                    </div>

                    {/* Work Type Name Field */}
                    <div className="w-full">
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Tên loại công việc
                        </label>
                        <InputSelection
                            options={listWorkTypes}
                            value={formData.workTypeId}
                            onChange={handleWorkChange}
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
                            Trạng thái công việc
                        </label>
                        <InputSelection
                            options={listStatus}
                            value={formData.status}
                            onChange={handleStatusChange}
                            placeholder="Trạng thái cũ"
                            disabled={loading}
                            className="w-full"
                        />
                    </div>

                    <div>
                        <DateTimePicker
                            label="Thời gian bắt đầu"
                            value={formData.startTime}
                            onChange={handleStartDateChange}
                            placeholder="Chọn thời gian bắt đầu"
                            disabled={loading}
                            className="w-full"
                        />
                    </div>

                    {/* End Date Picker */}
                    <div>
                        <DateTimePicker
                            label="Thời gian kết thúc"
                            value={formData.endTime}
                            onChange={handleEndDateChange}
                            placeholder="Chọn thời gian kết thúc"
                            disabled={loading}
                            className="w-full"
                        />
                    </div>

                    <div className="w-full">
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Chọn người thực hiện
                        </label>
                        <InputSelection
                            options={listUser}
                            value={formData.assignedUserId}
                            onChange={handleStatusChange}
                            placeholder="chọn người thực hiện"
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
                </form>
            </div>
        </div>
    );
};


export default WorkSearch;