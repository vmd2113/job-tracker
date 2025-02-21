import axiosInstance from "./axiosConfig.js";

const login = (username, password) => {
    const inputData = {
        username: username,
        password: password
    }
    console.log(inputData)
    return axiosInstance.post('/auth/login', inputData);
};

const register = (userData) => {
    const inputData = {
        username: userData.username,
        email: userData.email,
        password: userData.password,
        firstName: userData.firstName,
        lastName: userData.lastName,

    }
    return axiosInstance.post('/auth/register', inputData);
};

const getCurrentUser = () => {
    return axiosInstance.get('/auth/current-user');
}

//TODO: fix backend
const createUserByAdmin = (userData) => {
    const inputData = {
        username: userData.username,
        email: userData.email,
        password: userData.password,
        firstName: userData.firstName,
        lastName: userData.lastName,
        departmentId: userData.departmentId,
        status: userData.status
    }
    return axiosInstance.post('/auth/create-user-by-admin', inputData);
};

const logout = () => {
    localStorage.removeItem('token'); // Xóa token khỏi localStorage
    return Promise.resolve(); // Thực hiện logic backend nếu cần
};

export {
    login,
    register,
    logout,
    getCurrentUser,
    createUserByAdmin

};
