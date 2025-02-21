import React, {useRef, useState, useEffect, useCallback} from 'react';
import * as api from "../../api/departmentApi.js";


const UseDepartmentManagement = () => {

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [departments, setDepartments] = useState([]);
    const [totalItems, setTotalItems] = useState(0);
    const [pagination, setPagination] = useState({
        currentPage: 1,
        pageSize: 10
    });

    const [keySearch, setKeySearch] = useState({
        departmentName: '',
        departmentCode: '',
        sortBy: 'updateDate',
        sortDirection: 'asc'
    });

    const [allDepartments, setAllDepartments] = useState([]);

    const searchTimeout = useRef(null)

    // handle search and fetch departments
    const fetchDepartments = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.searchDepartments({
                page: pagination.currentPage,
                size: pagination.pageSize,
                ...keySearch
            });

            console.log("response at useDepartmentManagement.jsx", response);
            const items = response?.data?.items;
            const total = response?.data?.total;

            if (items) {
                setDepartments(items);
                setTotalItems(total);
            }

        } catch (error) {
            const errorMessage = error?.response?.data?.message || error.message || 'Error fetching departments';
            setError(errorMessage);


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
            departmentName: '',
            departmentCode: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
        });
        setPagination(prev => ({
            ...prev,
            currentPage: 1
        }));
    }, []);

    const fetchDepartmentWithHierarchy = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.getDepartmentWithHierarchy();
            console.log("response at useDepartmentManagement.jsx", response);
            return response;
        } catch (error) {
            const errorMessage = error?.response?.data?.message || error.message || 'Error fetching department with hierarchy';
            setError(errorMessage);
            throw error;
        } finally {
            setLoading(false)
        }
    }, [])

    const fetchAllDepartments = useCallback(async () => {
        try {
            setLoading(true)
            setError(null);

            const response = await api.getAllDepartments();
            console.log("USED DEPARTMENT MANAGEMENT.JSX FETCH ALL DEPARTMENTS", response?.data);

            const departments = response?.data;
            console.log("USED DEPARTMENT MANAGEMENT.JSX FETCH ALL DEPARTMENTS", departments);

            if (departments) {
                setAllDepartments(departments);
                return departments;
            }
            return [];
        } catch (error) {
            const errorMessage = error?.response?.data || error.message || 'Error fetching department';
            setError(errorMessage)
            throw error;
        } finally {
            setLoading(false);

        }

    }, [])


    // create department
    const createDepartment = useCallback(async (departmentData) => {
        try {
            setLoading(true);
            setError(null);

            const response = await api.createDepartment(departmentData);
            console.log("response at useDepartmentManagement.jsx", response);


            // Check if we have a response with data property
            await Promise.all([
                fetchDepartments(),
                fetchAllDepartments(),
                fetchDepartmentWithHierarchy()

            ]);
            if (response && response.data) {
                return response.data;
            }

            throw new Error('Invalid response format');
        } catch (err) {
            const errorMessage = err?.response?.data?.message || err.message || 'Error creating department';
            setError(errorMessage);
            throw err; // Throw the error to be handled by the component
        } finally {
            setLoading(false);
        }

    }, [fetchDepartments, fetchAllDepartments, fetchDepartmentWithHierarchy])

    const updateDepartment = useCallback(async (id, updateData) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.updateDepartment(id, updateData);

            await Promise.all([
                fetchDepartments(),
                fetchAllDepartments(),
                fetchDepartmentWithHierarchy()

            ]);
            return response?.data?.data;

        } catch (error) {
            const errorMessage = error?.response?.data?.message || error.message || 'Error updating department';
            setError(errorMessage)

            throw error;
        } finally {
            setLoading(false);
        }
    }, [fetchDepartments, fetchAllDepartments, fetchDepartmentWithHierarchy])

    const deleteDepartments = useCallback(async (id) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.deleteDepartment(id);
            console.log("response at useDepartmentManagement.jsx", response);

            await Promise.all([
                fetchDepartments(),
                fetchAllDepartments(),


            ]);

            return response;

        } catch (error) {
            const errorMessage = error?.response?.data?.message || error.message || 'Error deleting departments';
            setError(errorMessage);
            throw error
        } finally {
            setLoading(false)
        }
        setLoading(true);
        setError(null);


    }, [fetchDepartments, fetchAllDepartments, fetchDepartmentWithHierarchy])

    useEffect(() => {
        fetchDepartments();
    }, [fetchDepartments]);

    useEffect(() => {
        fetchAllDepartments();
    }, [fetchAllDepartments]);

    useEffect(() => {
        return () => {
            if (searchTimeout.current) {
                clearTimeout(searchTimeout.current);
            }
        };
    }, []);

    const deleteListDepartments = useCallback(async (ids) => {
        try {
            const response = await api.deleteListDepartments(ids);

            await Promise.all([
                fetchDepartments(),
                fetchAllDepartments(),
                fetchDepartmentWithHierarchy()

            ]);
            console.log("response at useDepartmentManagement.jsx", response);
            return response?.data;
        } catch (error) {
            const errorMessage = error?.response?.data?.message || error.message || 'Error deleting departments';
            setError(errorMessage);
            throw error
        } finally {
            setLoading(false)
        }
    }, [fetchDepartments, fetchAllDepartments])

    return {
        departments,
        allDepartments,
        loading,
        error,
        totalItems,
        pagination,
        keySearch,
        fetchDepartments,
        fetchAllDepartments,
        fetchDepartmentWithHierarchy,
        handleSearch,
        handlePageChange,
        handlePageSizeChange,
        resetFilters,
        createDepartment,
        updateDepartment,
        deleteDepartments

    }
};

export default UseDepartmentManagement;