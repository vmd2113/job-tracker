import React, {useCallback, useEffect, useRef, useState} from 'react';
import * as api from "../../api/workConfigBusiness.js";

const useWorkConfigManagement = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [workConfigs, setWorkConfigs] = useState([]);
    const [totalItems, setTotalItems] = useState(0);
    const [pagination, setPagination] = useState({
        currentPage: 1,
        pageSize: 10
    });

    const [keySearch, setKeySearch] = useState({
        workTypeId: '',
        priorityId: '',
        oldStatus: '',
        newStatus: '',
        sortBy: 'updateDate',
        sortDirection: 'asc'
    });

    const searchTimeout = useRef(null)

    const getErrorMessage = (error) => {
        console.log("ERROR - GET ERROR MESSAGE", error);
        if (error.response?.data) {
            return error.response.data.message;
        }
        return error.response.data.message || 'Có lỗi xảy ra. Vui lòng thử lại.';
    }

    // Fetch work configs with search criteria and pagination
    const fetchWorkConfigs = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            const response = await api.searchWorkConfig({


                page: pagination.currentPage,
                size: pagination.pageSize,
                ...keySearch
            });


            console.log("pagination.currentPage at useWorkConfigManagement.jsx", pagination.currentPage);
            console.log("response at useWorkConfigManagement.jsx", response);


            const items = response?.data?.items;
            const total = response?.data?.total;

            const serverPageNo = response?.data?.pageNo;
            console.log("Page no: ", serverPageNo);
            if (items) {
                setWorkConfigs(items);
                setTotalItems(total);

                if (serverPageNo && serverPageNo !== pagination.currentPage) {
                    setPagination(prev => ({...prev, currentPage: serverPageNo}));
                }
            }


        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK CONFIG: ", errorMsg)
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
            workTypeId: '',
            priorityId: '',
            oldStatus: '',
            newStatus: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
        });
        setPagination(prev => ({
            ...prev,
            currentPage: 1
        }));
    }, []);

    const createWorkConfig = useCallback(async (workConfigData) => {
        try {
            setLoading(true);
            setError(null);

            console.log("WORK CONFIG DATA", workConfigData);

            const response = await api.createWorkConfig(workConfigData);
            console.log("RESPONSE CREATE WORK CONFIG AT WORK CONFIG MANAGEMENT", response);

            if (response?.data) {
                console.log("DATA AFTER CREATE WORK CONFIG AT WORK CONFIG MANAGEMENT", response.data);
                await fetchWorkConfigs();
                return response.data;
            }

            // If we have a response but no data property, return the response itself
            if (response) {
                await fetchWorkConfigs(); // Refresh list after creation
                return response;
            }

        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK CONFIG: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorkConfigs]);


    const updateWorkConfig = useCallback(async (workConfigId, workConfigData) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.updateWorkConfig(workConfigId, workConfigData);
            console.log("RESPONSE AFTER UPDATE WORK CONFIG AT WORK CONFIG MANAGEMENT", response);
            await fetchWorkConfigs(); // Refresh list after update
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK CONFIG: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorkConfigs]);

    const deleteWorkConfig = useCallback(async (workConfigId) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.deleteWorkConfig(workConfigId);
            await fetchWorkConfigs(); // Refresh list after deletion
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK CONFIG: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorkConfigs]);


    const deleteListWorkConfigs = useCallback(async (workConfigIds) => {
        try {
            setLoading(true);
            setError(null);
            console.log("DELETE LIST WORK CONFIGS -  WORK CONFIG MANAGEMENT ", workConfigIds)
            const response = await api.deleteListWorkConfigs(workConfigIds);
            await fetchWorkConfigs(); // Refresh list after deletion
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK CONFIG: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorkConfigs]);

    useEffect(() => {
        fetchWorkConfigs();
    }, [fetchWorkConfigs]);

    useEffect(() => {
        return () => {
            if (searchTimeout.current) {
                clearTimeout(searchTimeout.current);
            }
        };
    }, []);

    return {
        workConfigs,
        loading,
        error,
        totalItems,
        pagination,
        keySearch,
        handleSearch,
        resetFilters,
        handlePageChange,
        handlePageSizeChange,
        createWorkConfig,
        updateWorkConfig,
        deleteWorkConfig,
        deleteListWorkConfigs
    }
};

export default useWorkConfigManagement;