import axiosInstance from "./axiosConfig.js";
import {useEffect} from "react";

const DEPARTMENT_API = '/cm/departments';



// fetch all original department
const getAllDepartments = async () => {
    try {
        const response = await axiosInstance.get(`${DEPARTMENT_API}/`);

        console.log("RESPONSE GET ALL DEPARTMENTS", response.data);

        return response.data;
    } catch (error) {
        throw error;
    }
};



// get department by id (not include parent/ child)
const getDepartmentById = async (id) => {
    try {
        const response = await axiosInstance.get(`${DEPARTMENT_API}/${id}`);
        return response.data;
    } catch (error) {
        throw error;
    }
}


// get department by name
const getDepartmentByName = async (name) => {
    try {
        const response = await axiosInstance.get(`${DEPARTMENT_API}/name/${name}`);
        return response.data;

    } catch (error) {
        throw error;
    }
}

// get department by code
const getDepartmentByCode = async (code) => {
    try {
        const response = await axiosInstance.get(`${DEPARTMENT_API}/code/${code}`);
        return response.data;

    } catch (error) {
        throw error;
    }
}

// create department
const createDepartment = async (values) => {
    console.log("CREATE DEPARTMENT -> DEPARTMENT SERVICE");
    try {
        console.log('Raw form values:', values); // Kiểm tra dữ liệu từ form
        const body = {
            departmentName: values.departmentName,
            departmentParentId: values.departmentParentId || null,
            departmentCode: values.departmentCode || null,
            status: values.status,
        };

        console.log('Request body:', body); // Kiểm tra dữ liệu trước khi gửi
        const response = await axiosInstance.post(`${DEPARTMENT_API}/`, body);
        console.log('Response:', response.data); // Kiểm tra response

        return response.data;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
};

const updateDepartment = async (id, departmentData) => {
    console.log("UPDATE DEPARTMENT -> DEPARTMENT SERVICE");
    console.log("ID UPDATE DEPARTMENT ", id);
    console.log("UPDATE DEPARTMENT DATA ", departmentData);
    try {
        const response = await axiosInstance.put(`${DEPARTMENT_API}/${id}`, departmentData);
        console.log("RESPONSE UPDATE DEPARTMENT ", response);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const deleteDepartment = async (id) => {
    try {

        console.log("DELETE DEPARTMENT -> DEPARTMENT SERVICE");
        console.log("ID DELETE DEPARTMENT ", id);
        const response = await axiosInstance.delete(`${DEPARTMENT_API}/${id}`);
        console.log("RESPONSE DELETE DEPARTMENT ", response.data);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const deleteListDepartments = async (ids) => {
    try {
        const response = await axiosInstance.delete(`${DEPARTMENT_API}/delete/list`, {
            data: ids
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getDepartmentWithHierarchy = async () => {
    try {

        console.log("GET DEPARTMENT WITH HIERARCHY -> DEPARTMENT SERVICE");
        const response = await axiosInstance.get(`${DEPARTMENT_API}/hierarchy`);
        console.log("response at getDepartmentWithHierarchy.jsx", response);
        return response.data;
    } catch (error) {
        throw error;
    }


}

const searchDepartments = async ({
                                     page = 0,
                                     size = 10,
                                     departmentName,
                                     departmentCode,
                                     sortBy = 'updateDate',
                                     sortDirection = 'asc'

                                 }) => {

    try {
        const params = {
            page: String(page),
            size: String(size),
            ...(departmentName && {departmentName}),
            ...(departmentCode && {departmentCode}),
            sortBy: sortBy || 'updateDate',
            sortDirection: sortDirection || 'asc'
        };

        const response = await axiosInstance.get(`${DEPARTMENT_API}/search`, {params});
        return response.data;


    } catch (error) {
        throw error;
    }

}



export {
    getAllDepartments,
    getDepartmentById,
    getDepartmentByName,
    getDepartmentByCode,
    getDepartmentWithHierarchy,
    createDepartment,
    updateDepartment,
    deleteDepartment,
    deleteListDepartments,
    searchDepartments
};


