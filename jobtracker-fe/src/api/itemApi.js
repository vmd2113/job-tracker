import axiosInstance from "./axiosConfig.js";

const ITEM_API = '/cm/items';

const getAllItems = async () => {
    try {
        const response = await axiosInstance.get(`${ITEM_API}` +'/');
        return response.data;
    } catch (error) {
        throw error;
    }
};


const getItemByItemId = async (itemId) => {
    try {
        const response = await axiosInstance.get(`${ITEM_API}/item/${itemId}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getItemByItemName = async (itemName) => {
    try {
        const response = await axiosInstance.get(`${ITEM_API}/itemName/${itemName}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getItemByItemCode = async (itemCode) => {
    try {
        const response = await axiosInstance.get(`${ITEM_API}/itemCode/${itemCode}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getItemByItemCategoryId = async (itemCategoryId) => {
    try {
        const response = await axiosInstance.get(`${ITEM_API}/itemCategory/${itemCategoryId}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getItemByItemCategoryName = async (itemCategoryName) => {
    try {
        const response = await axiosInstance.get(`${ITEM_API}/itemCategoryName/${itemCategoryName}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getItemByCategoryPriority = async () => {
    try {
        const response = await axiosInstance.get(`${ITEM_API}/category/priority`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getItemByCategoryStatus = async () => {
    try {
        const response = await axiosInstance.get(`${ITEM_API}/category/wo-status`);
        return response.data;
    } catch (error) {
        throw error;
    }
};



const createItem = async (itemData) => {

    try {
        const body = {
            itemName: itemData.itemName,
            itemCode: itemData.itemCode,
            itemValue: itemData.itemValue,
            itemParentId: itemData.itemParentId,
            categoryId: itemData.categoryId,
            status: itemData.status
        }
        const response = await axiosInstance.post(`${ITEM_API}/`, body);
        return response.data;
    }catch (error) {
        console.log("ERROR AT CREATE ITEM", error);
        throw error;
    }
}

const updateItem = async (itemId, itemData) => {
    try {
        const body = {
            itemName: itemData.itemName,
            itemCode: itemData.itemCode,
            itemValue: itemData.itemValue,
            itemParentId: itemData.itemParentId,
            categoryId: itemData.categoryId,
            status: itemData.status
        }
        const response = await axiosInstance.put(`${ITEM_API}/${itemId}`, body);
        return response.data;
    } catch (error) {
        console.log("ERROR AT UPDATE ITEM", error);
        throw error;
    }
}

const deleteItem = async (itemId) => {
    try {
        const response = await axiosInstance.delete(`${ITEM_API}/${itemId}`);
        return response.data;
    } catch (error) {
        console.log("ERROR AT DELETE ITEM", error);
        throw error;
    }
}

//Delete list items
const deleteListItems = async (itemIds) => {
    try {
        const response = await axiosInstance.delete(`${ITEM_API}/delete/list`, {
            data: itemIds
        });
        return response.data;
    } catch (error) {
        console.log("ERROR AT DELETE LIST ITEMS", error);
        throw error;
    }
}

const searchItems = async ({
                               page = 0,
                               size = 10,
                               itemName,
                               itemCode,
                               categoryCode,
                               itemParentId,
                               sortBy = 'updateDate',
                               sortDirection = 'asc'
                           }) => {
    try {
        const params = {
            page: String(page),
            size: String(size),
            ...(itemName && {itemName}),
            ...(itemCode && {itemCode}),
            ...(categoryCode && {categoryCode}),
            ...(itemParentId && {itemParentId}),
            sortBy: sortBy || 'updateDate',
            sortDirection: sortDirection || 'asc'
        };

        const response = await axiosInstance.get(`${ITEM_API}/search`, {params});
        return response.data;
    } catch (error) {
        console.log("ERROR AT SEARCH ITEMS", error);
        throw error;
    }
}



export {
    getAllItems,
    getItemByItemId,
    getItemByItemName,
    getItemByItemCode,
    getItemByItemCategoryId,
    getItemByItemCategoryName,
    getItemByCategoryStatus,
    getItemByCategoryPriority,
    createItem,
    updateItem,
    deleteItem,
    deleteListItems,
    searchItems
}


