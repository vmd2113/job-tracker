import axiosInstance from "./axiosConfig.js";

const USER_API = '/cm/users';

const getAllUsers = async () => {
    try {
        const response = await axiosInstance.get(`${USER_API}/`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getUserById = async (id) => {
    try {
        const response = await axiosInstance.get(`${USER_API}/${id}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getUserByUsername = async (username) => {
    try {
        const response = await axiosInstance.get(`${USER_API}/name`, {
            params: {username}
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getUserByEmail = async (email) => {
    try {
        const response = await axiosInstance.get(`${USER_API}/email`, {
            params: {email}
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getUserByPhoneNumber = async (phoneNumber) => {
    try {
        const response = await axiosInstance.get(`${USER_API}/phone`, {
            params: {phoneNumber}
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

const searchUsers = async ({
                               page = 0,
                               size = 10,
                               username,
                               email,
                               phoneNumber,
                               firstname,
                               sortBy,
                               sortDirection
                           }) => {
    try {
        const params = {
            page: String(page),
            size: String(size),
            ...(username && {username}),
            ...(email && {email}),
            ...(phoneNumber && {phoneNumber}),
            ...(firstname && {firstname}),
            sortBy: sortBy || 'updateDate',
            sortDirection: sortDirection || 'asc'
        };

        const response = await axiosInstance.get(`${USER_API}/search`, {params});
        return response.data;
    } catch (error) {
        throw error;
    }
};

// Trong userApi.js
// Trong userApi.js
const createUser = async (values) => {
    console.log("CREATE USER -> USER SERVICE");
    try {
        const body = {
            username: values.username,
            email: values.email,
            password: values.password,
            phoneNumber: values.phoneNumber,
            firstName: values.firstName,
            lastName: values.lastName,
            departmentId: values.departmentId || null,
            status: 1
        };

        const response = await axiosInstance.post(`${USER_API}/`, body);
        return response.data;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
};

const updateUser = async (id, values) => {
    try {

        const body = {
            username: values.username,
            email: values.email,
            password: values.password,
            phoneNumber: values.phoneNumber,
            firstName: values.firstName,
            lastName: values.lastName,
            departmentId: values.departmentId.value || null,
            status: 1
        };
        const response = await axiosInstance.put(`${USER_API}/${id}`, body);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const deleteUser = async (id) => {
    console.log("DELETE USER -> USER SERVICE");
    try {
        const response = await axiosInstance.delete(`${USER_API}/${id}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const deleteListUsers = async (ids) => {
    console.log("DELETE LIST USERS -> USER SERVICE");
    try {
        const response = await axiosInstance.delete(`${USER_API}/delete/list`, {
            data: ids
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

export {
    getAllUsers,
    getUserById,
    getUserByUsername,
    getUserByEmail,
    getUserByPhoneNumber,
    searchUsers,
    createUser,
    updateUser,
    deleteUser,
    deleteListUsers
};