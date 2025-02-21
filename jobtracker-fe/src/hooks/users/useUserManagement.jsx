import {useState, useEffect, useCallback, useRef} from 'react';
import * as api from "../../api/userApi.js";

const useUserManagement = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [users, setUsers] = useState([]);
    const [totalItems, setTotalItems] = useState(0);
    const [pagination, setPagination] = useState({
        currentPage: 1,
        pageSize: 5
    });

    const [searchCriteria, setSearchCriteria] = useState({
        username: '',
        email: '',
        phoneNumber: '',
        firstname: '',
        sortBy: 'updateDate',
        sortDirection: 'asc'
    });

    // For debouncing search requests
    const searchTimeout = useRef(null);


    const getErrorMessage = (error) => {
        console.log("ERROR - GET ERROR MESSAGE", error);
        if (error.response?.data) {
            return error.response.data.message;
        }
        return error.response.data.message || 'Có lỗi xảy ra. Vui lòng thử lại.';
    }

    // Fetch users with search criteria and pagination
    const fetchUsers = useCallback(async () => {
        console.log("UserManagement.jsx fetchUsers");
        try {
            setLoading(true);
            setError(null);

            const response = await api.searchUsers({

                page: pagination.currentPage,
                size: pagination.pageSize,
                ...searchCriteria
            });
            console.log("pagination.currentPage at useUserManagement.jsx", pagination.currentPage);
            console.log("response at useUserManagement.jsx", response);


            const items = response?.data?.items;
            const total = response?.data?.total;

            const serverPageNo = response?.data?.pageNo;
            console.log("Page no: ", serverPageNo);
            if (items) {
                setUsers(items);
                setTotalItems(total);

                if (serverPageNo && serverPageNo !== pagination.currentPage) {
                    setPagination(prev => ({...prev, currentPage: serverPageNo}));
                }
            }


        } catch (err) {
            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH USER: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [pagination.currentPage, pagination.pageSize, searchCriteria]);

    // Handle search criteria changes with debouncing
    const handleSearch = useCallback((newCriteria) => {
        setSearchCriteria(prev => ({
            ...prev,
            ...newCriteria
        }));
        setPagination(prev => ({...prev, currentPage: 1}));
    }, []);

    // Handle page change
    const handlePageChange = useCallback((newPage) => {
        // newPage is already 1-based from the Pagination component
        setPagination(prev => ({...prev, currentPage: newPage}));
    }, []);

    // Handle page size change
    const handlePageSizeChange = useCallback((newSize) => {
        setPagination(prev => ({
            ...prev,
            pageSize: newSize,
            currentPage: 1
        }));
    }, []);

    // Reset all filters and pagination
    const resetFilters = useCallback(() => {
        setSearchCriteria({
            username: '',
            email: '',
            phoneNumber: '',
            firstname: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
        });
        setPagination(prev => ({
            ...prev,
            currentPage: 1
        }));
    }, []);

    const fetchAllUser = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.getAllUsers()
            console.log("USED USER MANAGEMENT.JSX FETCH ALL USERS", response);
            return response?.data;
        } catch (err) {
            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH USER: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, []);


    // Create new user
    const createUser = useCallback(async (userData) => {
        console.log("CREATE USER -  USER MANAGEMENT ")
        try {
            setLoading(true);
            setError(null);
            const response = await api.createUser(userData);
            console.log("RESPONSE CREATE USER AT USER MANAGEMENT", response);

            if (response?.data) {
                console.log("DATA AFTER CREATE USER AT USER MANAGEMENT", response.data);
                await fetchUsers();
                return response.data;
            }

            // If we have a response but no data property, return the response itself
            if (response) {
                await fetchUsers(); // Refresh list after creation
                return response;
            }

        } catch (err) {

            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH USER: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchUsers]);


    // Update existing user
    const updateUser = useCallback(async (id, userData) => {
        try {
            setLoading(true);
            setError(null);

            console.log("DATA UPDATE USER: ", userData)


            const response = await api.updateUser(id, userData);
            console.log("RESPONSE AFTER UPDATE USER AT USER MANAGEMENT", response);
            await fetchUsers(); // Refresh list after update
            return response;
        } catch (err) {

            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH USER: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchUsers]);

    // Delete user
    const deleteUser = useCallback(async (id) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.deleteUser(id);
            await fetchUsers(); // Refresh list after deletion
            return response;
        } catch (err) {
            // Enhanced error extraction
            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH USER: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);

        } finally {
            setLoading(false);
        }
    }, [fetchUsers]);


    const deleteListUsers = useCallback(async (ids) => {
        try {
            setLoading(true);
            setError(null);
            console.log("DELETE LIST USERS -  USER MANAGEMENT ", ids)
            const response = await api.deleteListUsers(ids);
            await fetchUsers(); // Refresh list after deletion
            return response;
        } catch (err) {
            // Enhanced error extraction
            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH USER: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchUsers]);

    // Get user by ID
    const getUserById = useCallback(async (id) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.getUserById(id);
            return response?.data?.data;
        } catch (err) {
            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH USER: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, []);

    // Reset all filters and pagination

    // Fetch users on mount and when dependencies change
    useEffect(() => {
        fetchUsers();
    }, [fetchUsers]);

    // Cleanup timeout on unmount
    useEffect(() => {
        return () => {
            if (searchTimeout.current) {
                clearTimeout(searchTimeout.current);
            }
        };
    }, []);

    return {
        users,
        loading,
        error,
        totalItems,
        pagination,
        searchCriteria,
        handleSearch,
        handlePageChange,
        handlePageSizeChange,
        createUser,
        updateUser,
        deleteUser,
        deleteListUsers,
        getUserById,
        fetchAllUser,
        resetFilters
    };
};

export default useUserManagement;