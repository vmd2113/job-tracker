import React, {useCallback, useEffect, useRef, useState} from 'react';
import * as api from "../../api/itemApi.js";
const useItemManagement = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [items, setItems] = useState([]);
    const [totalItems, setTotalItems] = useState(0);
    const [pagination, setPagination] = useState({
        currentPage: 1,
        pageSize: 10
    });

    const [keySearch, setKeySearch] = useState({
        itemName: '',
        itemCode: '',
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

    // Fetch items with search criteria and pagination
    const fetchItems = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            const response = await api.searchItems({

                page: pagination.currentPage,
                size: pagination.pageSize,
                ...keySearch
            });
            console.log("pagination.currentPage at useItemManagement.jsx", pagination.currentPage);
            console.log("response at useItemManagement.jsx", response);


            const items = response?.data?.items;
            const total = response?.data?.total;

            const serverPageNo = response?.data?.pageNo;
            console.log("Page no: ", serverPageNo);
            if (items) {
                setItems(items);
                setTotalItems(total);

                if (serverPageNo && serverPageNo !== pagination.currentPage) {
                    setPagination(prev => ({...prev, currentPage: serverPageNo}));
                }
            }


        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH ITEM: ", errorMsg)
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
            itemName: '',
            itemCode: '',
            itemCategoryId: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
        });
        setPagination(prev => ({
            ...prev,
            currentPage: 1
        }));
    }, []);

    const getItemByCategoryPriority = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.getItemByCategoryPriority();
            console.log("USED ITEM MANAGEMENT.JSX GET ITEM BY CATEGORY PRIORITY", response);
            return response?.data;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR GET ITEM BY CATEGORY PRIORITY: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, []);

    const getItemByCategoryStatus = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.getItemByCategoryStatus();
            console.log("USED ITEM MANAGEMENT.JSX GET ITEM BY CATEGORY STATUS", response);
            return response?.data;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR GET ITEM BY CATEGORY STATUS: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, []);



    const createItem = useCallback(async (itemData) => {
        try {
            setLoading(true);
            setError(null);

            const response = await api.createItem(itemData);
            console.log("RESPONSE CREATE ITEM AT ITEM MANAGEMENT", response);

            if (response?.data) {
                console.log("DATA AFTER CREATE ITEM AT ITEM MANAGEMENT", response.data);
                await fetchItems();
                return response.data;
            }

            // If we have a response but no data property, return the response itself
            if (response) {
                await fetchItems(); // Refresh list after creation
                return response;
            }

        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH ITEM: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchItems]);


    const updateItem = useCallback(async (itemId, itemData) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.updateItem(itemId, itemData);
            console.log("RESPONSE AFTER UPDATE ITEM AT ITEM MANAGEMENT", response);
            await fetchItems(); // Refresh list after update
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH ITEM: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchItems]);

    const deleteItem = useCallback(async (itemId) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.deleteItem(itemId);
            await fetchItems(); // Refresh list after deletion
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH ITEM: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchItems]);


    const deleteListItems = useCallback(async (itemIds) => {
        try {
            setLoading(true);
            setError(null);
            console.log("DELETE LIST ITEMS -  ITEM MANAGEMENT ", itemIds)
            const response = await api.deleteListItems(itemIds);
            await fetchItems(); // Refresh list after deletion
            return response;
        } catch (error) {
            const errorMsg = getErrorMessage(error);
            console.log("ERROR FETCH SEARCH ITEM: ", errorMsg)
            setError(errorMsg);
            throw new Error(errorMsg);
        } finally {
            setLoading(false);
        }
    }, [fetchItems]);

    useEffect(() => {
        fetchItems();
    }, [fetchItems]);

    useEffect(() => {
        return () => {
            if (searchTimeout.current) {
                clearTimeout(searchTimeout.current);
            }
        };
    }, []);

    return {
        items,
        loading,
        error,
        totalItems,
        pagination,
        keySearch,
        fetchItems,
        getItemByCategoryPriority,
        getItemByCategoryStatus,
        handleSearch,
        handlePageChange,
        handlePageSizeChange,
        resetFilters,
        createItem,
        updateItem,
        deleteItem,
        deleteListItems
    }
};

export default useItemManagement;