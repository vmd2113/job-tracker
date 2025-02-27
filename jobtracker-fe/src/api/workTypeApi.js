import axiosInstance from "./axiosConfig.js";

const WORK_TYPE_API = '/wfm/work-type';

const getAllWorkTypes = async () => {
    try {
        const response = await axiosInstance.get(`${WORK_TYPE_API}/`);
        console.log("RESPONSE GET ALL WORK TYPES", response);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getWorkTypeById = async (workTypeId) => {
    try {
        const response = await axiosInstance.get(`${WORK_TYPE_API}/${workTypeId}`);
        return response.data;
    } catch (error) {
        throw error;
    }
}


// Create work type
const createWorkType = async (workTypeData) => {

    try {
        // Đảm bảo processTime là số float
        const processTime = workTypeData.processTime !== null &&
        workTypeData.processTime !== undefined ?
            parseFloat(workTypeData.processTime) : null;

        // Nếu processTime là null hoặc NaN, báo lỗi luôn
        if (processTime === null || isNaN(processTime)) {
            throw new Error("Thời gian xử lý không hợp lệ");
        }

        console.log("processTime", processTime);


        const body = {
            workTypeName: workTypeData.workTypeName,
            workTypeCode: workTypeData.workTypeCode,
            processTime: processTime,
            status: workTypeData.status,
            priorityId: workTypeData.priorityId || null
        }

        console.log("BODY", body);
        const response = await axiosInstance.post(`${WORK_TYPE_API}/`, body);
        return response.data;
    } catch (error) {
        console.log("ERROR AT CREATE WORK TYPE", error);
        throw error;
    }
}

const updateWorkType = async (workTypeId, workTypeData) => {
    try {

        const processTime = workTypeData.processTime !== null &&
        workTypeData.processTime !== undefined ?
            parseFloat(workTypeData.processTime) : null;

        // Nếu processTime là null hoặc NaN, báo lỗi luôn
        if (processTime === null || isNaN(processTime)) {
            throw new Error("Thời gian xử lý không hợp lệ");
        }
        console.log("processTime", processTime);

        const body = {
            workTypeName: workTypeData.workTypeName,
            workTypeCode: workTypeData.workTypeCode,
            processTime: processTime,
            status: workTypeData.status,
            priorityId: workTypeData.priorityId || null
        }

        console.log("BODY", body);
        const response = await axiosInstance.put(`${WORK_TYPE_API}/${workTypeId}`, body);
        console.log("RESPONSE AT UPDATE WORK TYPE", response);
        return response.data;
    } catch (error) {
        console.log("ERROR AT UPDATE WORK TYPE", error);
        throw error;
    }
}


//search work type
const searchWorkTypes = async ({
                                   page = 0,
                                   size = 10,
                                   workTypeName,
                                   workTypeCode,
                                   sortBy = 'updateDate',
                                   sortDirection = 'asc'
                               }) => {
    try {
        const params = {
            page: String(page),
            size: String(size),
            ...(workTypeName && {workTypeName}),
            ...(workTypeCode && {workTypeCode}),
            sortBy: sortBy || 'updateDate',
            sortDirection: sortDirection || 'desc'
        };

        const response = await axiosInstance.get(`${WORK_TYPE_API}/search`, {params});
        return response.data;
    } catch (error) {
        console.log("ERROR AT SEARCH WORK TYPES", error);
        throw error;
    }
}

//Delete work type
const deleteWorkType = async (workTypeId) => {
    try {
        const response = await axiosInstance.delete(`${WORK_TYPE_API}/${workTypeId}`);
        return response.data;
    } catch (error) {
        console.log("ERROR AT DELETE WORK TYPE", error);
        throw error;
    }
}

const deleteListWorkTypes = async (workTypeIds) => {
    try {
        const response = await axiosInstance.delete(`${WORK_TYPE_API}/delete/list`, {
            data: workTypeIds
        });
        return response.data;
    } catch (error) {
        console.log("ERROR AT DELETE LIST WORK TYPES", error);
        throw error;
    }
}

export {
    getAllWorkTypes,
    getWorkTypeById,
    searchWorkTypes,
    createWorkType,
    updateWorkType,
    deleteWorkType,
    deleteListWorkTypes
}