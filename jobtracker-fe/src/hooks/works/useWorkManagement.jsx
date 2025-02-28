import React, {useCallback, useEffect, useRef, useState} from 'react';
import * as api from "../../api/worksApi.js";

const useWorkManagement = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [works, setWorks] = useState([]);
    const [totalItems, setTotalItems] = useState(0);
    const [pagination, setPagination] = useState({
        currentPage: 1,
        pageSize: 10
    });

    const [keySearch, setKeySearch] = useState({
        workCode: '',
        workContent: '',
        workTypeId: '',
        priorityId: '',
        startTime: '',
        endTime: '',
        assignedUserId: '',
        sortBy: 'updateDate',
        sortDirection: 'desc'
    });

    const searchTimeout = useRef(null);

    const getErrorMessage = (error) => {
        if (error.response?.data?.message) {
            return error.response.data.message;
        }
        return 'Có lỗi xảy ra. Vui lòng thử lại.';
    };

    // Fetch works with search criteria and pagination
    const fetchWorks = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            const response = await api.searchWorks({
                page: pagination.currentPage,
                size: pagination.pageSize,
                ...keySearch
            });

            const items = response?.data?.items;
            const total = response?.data?.total;

            const serverPageNo = response?.data?.pageNo;

            if (items) {
                setWorks(items);
                setTotalItems(total);

                if (serverPageNo && serverPageNo !== pagination.currentPage) {
                    setPagination(prev => ({...prev, currentPage: serverPageNo}));
                }
            }
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.error("ERROR FETCH SEARCH WORK: ", errorMsg);
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [pagination.currentPage, pagination.pageSize, keySearch]);

    const handleSearch = useCallback((keySearch) => {
        setKeySearch(prev => ({
            ...prev,
            ...keySearch
        }));
        setPagination(prev => ({...prev, currentPage: 1}));
    }, []);

    const handlePageChange = useCallback((newPage) => {
        setPagination(prev => ({...prev, currentPage: newPage}));
    }, []);

    const handlePageSizeChange = useCallback((newSize) => {
        setPagination(prev => ({
            ...prev,
            pageSize: newSize,
            currentPage: 1
        }));
    }, []);

    const resetFilters = useCallback(() => {
        setKeySearch({
            workCode: '',
            workContent: '',
            workTypeId: '',
            priorityId: '',
            startTime: '',
            endTime: '',
            assignedUserId: '',
            sortBy: 'updateDate',
            sortDirection: 'desc'
        });
        setPagination(prev => ({
            ...prev,
            currentPage: 1
        }));
    }, []);

    // Format date for API request if needed
    const formatDate = (dateObj) => {
        if (!dateObj) return '';
        // If it's already a string in ISO format, return it
        if (typeof dateObj === 'string') return dateObj;
        // Otherwise convert to ISO string
        return dateObj.toISOString();
    };

    const createWork = useCallback(async (workData) => {
        try {
            setLoading(true);
            setError(null);

            // Ensure dates are properly formatted
            const formattedWorkData = {
                ...workData,
                startTime: formatDate(workData.startTime),
                endTime: formatDate(workData.endTime)
            };

            const response = await api.createWork(formattedWorkData);

            if (response) {
                await fetchWorks(); // Refresh list after creation
                return response;
            }
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.error("ERROR CREATING WORK: ", errorMsg);
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorks]);

    const updateWork = useCallback(async (workId, workData) => {
        try {
            setLoading(true);
            setError(null);

            // Ensure dates are properly formatted
            const formattedWorkData = {
                ...workData,
                startTime: formatDate(workData.startTime),
                endTime: formatDate(workData.endTime)
            };

            const response = await api.updateWork(workId, formattedWorkData);
            await fetchWorks(); // Refresh list after update
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.error("ERROR UPDATING WORK: ", errorMsg);
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorks]);

    const deleteWork = useCallback(async (workId) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.deleteWork(workId);
            await fetchWorks(); // Refresh list after deletion
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.error("ERROR DELETING WORK: ", errorMsg);
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorks]);

    const deleteListWorks = useCallback(async (workIds) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.deleteListWorks(workIds);
            await fetchWorks(); // Refresh list after deletion
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.error("ERROR DELETING WORKS: ", errorMsg);
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorks]);

    useEffect(() => {
        fetchWorks();
    }, [fetchWorks]);

    useEffect(() => {
        return () => {
            if (searchTimeout.current) {
                clearTimeout(searchTimeout.current);
            }
        };
    }, []);

    return {
        works,
        loading,
        error,
        totalItems,
        pagination,
        keySearch,
        handleSearch,
        resetFilters,
        handlePageChange,
        handlePageSizeChange,
        createWork,
        updateWork,
        deleteWork,
        deleteListWorks
    };
};

export default useWorkManagement;