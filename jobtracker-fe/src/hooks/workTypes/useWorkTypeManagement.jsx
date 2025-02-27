import React, {useCallback, useRef, useState, useEffect} from 'react';
import * as api from "../../api/workTypeApi.js";
const useWorkTypeManagement = () => {

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [workTypes, setWorkTypes] = useState([]);
    const [totalItems, setTotalItems] = useState(0);
    const [pagination, setPagination] = useState({
        currentPage: 1,
        pageSize: 10
    });

    const [keySearch, setKeySearch] = useState({
        workTypeName: '',
        workTypeCode: '',
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

    // Fetch work types with search criteria and pagination
    const fetchWorkTypes = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            const response = await api.searchWorkTypes({

                page: pagination.currentPage,
                size: pagination.pageSize,
                ...keySearch
            });
            console.log("pagination.currentPage at useWorkTypeManagement.jsx", pagination.currentPage);
            console.log("response at useWorkTypeManagement.jsx", response);


            const items = response?.data?.items;
            const total = response?.data?.total;

            const serverPageNo = response?.data?.pageNo;
            console.log("Page no: ", serverPageNo);
            if (items) {
                setWorkTypes(items);
                setTotalItems(total);

                if (serverPageNo && serverPageNo !== pagination.currentPage) {
                    setPagination(prev => ({...prev, currentPage: serverPageNo}));
                }
            }


        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK TYPE: ", errorMsg)
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
            workTypeName: '',
            workTypeCode: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
        });
        setPagination(prev => ({
            ...prev,
            currentPage: 1
        }));
    }, []);


    const fetchAllWorkTypes = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.getAllWorkTypes();
            console.log("USED WORK TYPE MANAGEMENT.JSX FETCH ALL WORK TYPES", response);
            return response?.data;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH ALL WORK TYPES: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, []);

    const createWorkType = useCallback(async (workTypeData) => {
        console.log("CREATE WORK TYPE -  WORK TYPE MANAGEMENT ")
        console.log("CREATE WORK TYPE DATA ", workTypeData);
        try {
            setLoading(true);
            setError(null);

            const response = await api.createWorkType(workTypeData);
            console.log("RESPONSE CREATE WORK TYPE AT WORK TYPE MANAGEMENT", response);

            if (response?.data) {
                console.log("DATA AFTER CREATE WORK TYPE AT WORK TYPE MANAGEMENT", response.data);
                await fetchWorkTypes();
                return response.data;
            }

            // If we have a response but no data property, return the response itself
            if (response) {
                await fetchWorkTypes(); // Refresh list after creation
                return response;
            }

        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK TYPE: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorkTypes]);


    const updateWorkType = useCallback(async (workTypeId, workTypeData) => {
        console.log("UPDATE WORK TYPE -  WORK TYPE MANAGEMENT ")
        console.log("UPDATE WORK TYPE DATA ", workTypeData);
        try {
            setLoading(true);
            setError(null);
            const response = await api.updateWorkType(workTypeId, workTypeData);
            console.log("RESPONSE AFTER UPDATE WORK TYPE AT WORK TYPE MANAGEMENT", response);
            await fetchWorkTypes(); // Refresh list after update
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK TYPE: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorkTypes]);

    const deleteWorkType = useCallback(async (workTypeId) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.deleteWorkType(workTypeId);
            await fetchWorkTypes(); // Refresh list after deletion
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK TYPE: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorkTypes]);


    const deleteListWorkTypes = useCallback(async (workTypeIds) => {
        try {
            setLoading(true);
            setError(null);
            console.log("DELETE LIST WORK TYPES -  WORK TYPE MANAGEMENT ", workTypeIds)
            const response = await api.deleteListWorkTypes(workTypeIds);
            await fetchWorkTypes(); // Refresh list after deletion
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH WORK TYPE: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchWorkTypes]);

    useEffect(() => {
        fetchWorkTypes();
    }, [fetchWorkTypes]);

    useEffect(() => {
        return () => {
            if (searchTimeout.current) {
                clearTimeout(searchTimeout.current);
            }
        };
    }, []);

    return {
        workTypes,
        loading,
        error,
        totalItems,
        pagination,
        keySearch,
        handleSearch,
        resetFilters,
        handlePageChange,
        handlePageSizeChange,
        fetchAllWorkTypes,
        createWorkType,
        updateWorkType,
        deleteWorkType,
        deleteListWorkTypes
    }
};

export default useWorkTypeManagement;