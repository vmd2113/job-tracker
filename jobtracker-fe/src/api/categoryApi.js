import axiosInstance from "./axiosConfig.js";

const CATEGORY_API = '/cm/categories/';

const getAllCategories = async () => {
    try {
        const response = await axiosInstance.get(`${CATEGORY_API}/`);
        console.log("RESPONSE GET ALL CATEGORIES", response.data);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getCategoryById = async (id) => {
    try {
        const response = await axiosInstance.get(`${CATEGORY_API}/${id}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};


const getCategoryByCategoryName = async (categoryName) => {
    try {
        const response = await axiosInstance.get(`${CATEGORY_API}/name/${categoryName}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getCategoryByCategoryCode = async (categoryCode) => {
    try {
        const response = await axiosInstance.get(`${CATEGORY_API}/code/${categoryCode}`);
        return response.data;
    } catch (error) {
        throw error;
    }
}


const createCategory = async (data) => {
    console.log("CREATE CATEGORY -> CATEGORY SERVICE");
    try {
        console.log('Raw form values:', data);
        const body = {
            categoryName: data.categoryName,
            categoryCode: data.categoryCode,
            status: 1

        };

        const response = await axiosInstance.post(`${CATEGORY_API}`, body);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const updateCategory = async (id, data) => {
    try {
        const response = await axiosInstance.put(`${CATEGORY_API}/${id}`, data);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const deleteCategory = async (id) => {
    try {
        const response = await axiosInstance.delete(`${CATEGORY_API}/${id}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const deleteListCategories = async (ids) => {
    try {
        const response = await axiosInstance.delete(`${CATEGORY_API}/delete/list`, {
            data: ids
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

const searchCategories = async ({
                                    page = 0,
                                    size = 10,
                                    categoryName,
                                    categoryCode,
                                    sortBy = 'updateDate',
                                    sortDirection = 'asc'
                                }) => {
    try {
        const params = {
            page: String(page),
            size: String(size),
            ...(categoryName && {categoryName}),
            ...(categoryCode && {categoryCode}),
            sortBy: sortBy || 'updateDate',
            sortDirection: sortDirection || 'asc'
        };
        console.log("params", params);

        const response = await axiosInstance.get(`${CATEGORY_API}/search`, {params});
        return response.data;
    } catch (error) {
        throw error;
    }

}

export {
    getAllCategories,
    getCategoryById,
    getCategoryByCategoryName,
    getCategoryByCategoryCode,
    searchCategories,
    createCategory,
    updateCategory,
    deleteCategory,
    deleteListCategories
};


