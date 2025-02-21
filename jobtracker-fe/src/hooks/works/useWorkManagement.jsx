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
        workTypeId: '',
        priorityId: '',
        startTime: '',
        endTime: '',
        assignedUserId: '',
        sortBy: 'updateDate',
        sortDirection: 'desc'
    });

    const searchTimeout = useRef(null)

    const getErrorMessage = (error) => {
        console.log("ERROR - GET ERROR MESSAGE", error);
        if (error.response?.data) {
            return error.response.data.message;
        }
        return error.response.data.message || 'Có lỗi xảy ra. Vui lòng thử lại.';
    }

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
            console.log("pagination.currentPage at useWorkManagement.jsx", pagination.currentPage);
            console.log("response at useWorkManagement.jsx", response);


            const items = response?.data?.items;
            const total = response?.data?.total;

            const serverPageNo = response?.data?.pageNo;
            console.log("Page no: ", serverPageNo);
            if (items) {
                setWorks(items);
                setTotalItems(total);

                if (serverPageNo && serverPageNo !== pagination.currentPage) {
                    setPagination(prev => ({...prev, currentPage: serverPageNo}));
                }
            }


        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK: ", errorMsg)
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
        // newPage is already 1-based from the Pagination component
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
            workTypeId: '',
            priorityId: '',
            startTime: '',
            endTime: '',
            assignedUserId: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
        });
        setPagination(prev => ({
            ...prev,
            currentPage: 1
        }));
    }, []);

    const createWork = useCallback(async (workData) => {
        try {
            setLoading(true);
            setError(null);

            const response = await api.createWork(workData);
            console.log("RESPONSE CREATE WORK AT WORK MANAGEMENT", response);

            if (response?.data) {
                console.log("DATA AFTER CREATE WORK AT WORK MANAGEMENT", response.data);
                await fetchWorks();
                return response.data;
            }

            // If we have a response but no data property, return the response itself
            if (response) {
                await fetchWorks(); // Refresh list after creation
                return response;
            }

        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK: ", errorMsg)
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
            const response = await api.updateWork(workId, workData);
            console.log("RESPONSE AFTER UPDATE WORK AT WORK MANAGEMENT", response);
            await fetchWorks(); // Refresh list after update
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK: ", errorMsg)
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
            console.log("ERROR FETCH SEARCH WORK: ", errorMsg)
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
            console.log("DELETE LIST WORKS -  WORK MANAGEMENT ", workIds)
            const response = await api.deleteListWorks(workIds);
            await fetchWorks(); // Refresh list after deletion
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK: ", errorMsg)
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


    }
};
export default useWorkManagement;