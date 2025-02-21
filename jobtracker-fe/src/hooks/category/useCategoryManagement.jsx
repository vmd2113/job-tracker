import {useState, useEffect, useCallback, useRef} from 'react';
import * as api from "../../api/categoryApi.js";

const useCategoryManagement = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [categories, setCategories] = useState([]);
    const [totalItems, setTotalItems] = useState(0);
    const [pagination, setPagination] = useState({
        currentPage: 1,
        pageSize: 10
    });

    const [keySearch, setKeySearch] = useState({
        categoryName: '',
        categoryCode: '',
        sortBy: 'updateDate',
        sortDirection: 'asc'
    });

    const searchTimeout = useRef(null);

    const getErrorMessage = (error) => {
        console.log("ERROR - GET ERROR MESSAGE", error);
        if (error.response?.data) {
            return error.response.data.message;
        }
        return error.response.data.message || 'Có lỗi xảy ra. Vui lòng thử lại.';
    }

    // Fetch categories with search criteria and pagination
    const fetchCategories = useCallback(async () => {
        console.log("CategoryManagement.jsx fetchCategories");
        try {
            setLoading(true);
            setError(null);

            const response = await api.searchCategories({

                page: pagination.currentPage,
                size: pagination.pageSize,
                ...keySearch
            });
            console.log("pagination.currentPage at useCategoryManagement.jsx", pagination.currentPage);
            console.log("response at useCategoryManagement.jsx", response);


            const items = response?.data?.items;
            const total = response?.data?.total;

            const serverPageNo = response?.data?.pageNo;
            console.log("Page no: ", serverPageNo);
            if (items) {
                setCategories(items);
                setTotalItems(total);

                if (serverPageNo && serverPageNo !== pagination.currentPage) {
                    setPagination(prev => ({...prev, currentPage: serverPageNo}));
                }
            }


        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH CATEGORY: ", errorMsg)
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
            name: '',
            categoryCode: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
        });
        setPagination(prev => ({
            ...prev,
            currentPage: 1
        }));
    }, []);


    // fetch all categories
    const fetchAllCategories = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.getAllCategories();
            console.log("RESPONSE FETCH ALL CATEGORIES", response);
            return response?.data;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH ALL CATEGORIES: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, []);

    // Create new category
    const createCategory = useCallback(async (categoryData) => {
        console.log("CREATE CATEGORY -  CATEGORY MANAGEMENT ")
        try {
            setLoading(true);
            setError(null);

            const response = await api.createCategory(categoryData);
            console.log("RESPONSE CREATE CATEGORY AT CATEGORY MANAGEMENT", response);

            if (response?.data) {
                console.log("DATA AFTER CREATE CATEGORY AT CATEGORY MANAGEMENT", response.data);
                await fetchCategories();
                return response.data;
            }

            // If we have a response but no data property, return the response itself
            if (response) {
                await fetchCategories(); // Refresh list after creation
                return response;
            }

        } catch (err) {

            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH CATEGORY: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchCategories]);


    // Update existing category
    const updateCategory = useCallback(async (id, categoryData) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.updateCategory(id, categoryData);
            console.log("RESPONSE AFTER UPDATE CATEGORY AT CATEGORY MANAGEMENT", response);
            await fetchCategories(); // Refresh list after update
            return response;
        } catch (err) {

            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH CATEGORY: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchCategories]);

    // Delete category
    const deleteCategory = useCallback(async (id) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.deleteCategory(id);
            await fetchCategories(); // Refresh list after deletion
            return response;
        } catch (err) {
            // Enhanced error extraction
            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH CATEGORY: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);

        } finally {
            setLoading(false);
        }
    }, [fetchCategories]);


    const deleteListCategories = useCallback(async (ids) => {
        try {
            setLoading(true);
            setError(null);
            console.log("DELETE LIST CATEGORY -  CATEGORY MANAGEMENT ", ids)
            const response = await api.deleteListCategories(ids);
            await fetchCategories(); // Refresh list after deletion
            return response;
        } catch (err) {
            // Enhanced error extraction
            const errorMsg = getErrorMessage(err);
            console.log("ERROR FETCH SEARCH CATEGORY: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchCategories]);

    useEffect(() => {
        fetchCategories();
    }, [fetchCategories]);

    useEffect(() => {
        return () => {
            if (searchTimeout.current) {
                clearTimeout(searchTimeout.current);
            }
        };
    }, []);


    return {
        categories,
        loading,
        error,
        totalItems,
        pagination,
        keySearch,
        fetchCategories,
        fetchAllCategories,
        handleSearch,
        handlePageChange,
        handlePageSizeChange,
        resetFilters,
        createCategory,
        updateCategory,
        deleteCategory,
        deleteListCategories
    }
};

export default useCategoryManagement;