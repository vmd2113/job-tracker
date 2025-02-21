import axiosInstance from "./axiosConfig.js";

const WORKS_CONFIG_API = '/wfm/work-config';

const getAllWorksConfig = async () => {
    try {
        const response = await axiosInstance.get(`${WORKS_CONFIG_API}/`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

const getWorkConfigById = async (workId) => {
    try {
        const response = await axiosInstance.get(`${WORKS_CONFIG_API}/${workId}`);
        return response.data;
    } catch (error) {
        throw error;
    }
}

const searchWorkConfig = async ({
                                   page = 0,
                                   size = 10,
                                   workTypeName,
                                   priorityId,
                                   oldStatus,
                                   newStatus,
                                   sortBy = 'updateDate',
                                   sortDirection = 'asc'
                               }) => {
    try {
        const params = {
            page: String(page),
            size: String(size),
            ...(workTypeName && {workTypeName}),
            ...(priorityId && {priorityId}),
            ...(oldStatus && {oldStatus}),
            ...(newStatus && {newStatus}),
            sortBy: sortBy || 'updateDate',
            sortDirection: sortDirection || 'asc'
        };

        const response = await axiosInstance.get(`${WORKS_CONFIG_API}/search`, {params});
        return response.data;
    } catch (error) {
        console.log("ERROR AT SEARCH WORK CONFIG", error);
        throw error;
    }
}



// Create work config
const createWorkConfig = async (workConfigData) => {
    try{
        const body = {
            workTypeId: workConfigData.workTypeId,
            priorityId: workConfigData.priorityId,
            oldStatusId: workConfigData.oldStatusId,
            newStatusId: workConfigData.newStatusId,

        }
        const response = await axiosInstance.post(`${WORKS_CONFIG_API}/`, body);
        return response.data;
    }catch (error) {
        console.log("ERROR AT CREATE WORK CONFIG", error);
        throw error;
    }
}

const updateWorkConfig = async (workConfigId, workConfigData) => {
    try {
        const body = {
            workTypeId: workConfigData.workTypeId,
            priorityId: workConfigData.priorityId,
            oldStatusId : workConfigData.oldStatusId,
            newStatusId: workConfigData.newStatusId,

        }
        const response = await axiosInstance.put(`${WORKS_CONFIG_API}/${workConfigId}`, body);
        return response.data;
    }catch (error) {
        console.log("ERROR AT UPDATE WORK CONFIG", error);
        throw error;
    }
}


//Delete work config
const deleteWorkConfig = async (workConfigId) => {
    try {
        const response = await axiosInstance.delete(`${WORKS_CONFIG_API}/${workConfigId}`);
        return response.data;
    } catch (error) {
        console.log("ERROR AT DELETE WORK CONFIG", error);
        throw error;
    }
}

const deleteListWorkConfigs = async (workConfigIds) => {
    try {
        const response = await axiosInstance.delete(`${WORKS_CONFIG_API}/delete/list`, {
            data: workConfigIds
        });
        return response.data;
    } catch (error) {
        console.log("ERROR AT DELETE LIST WORK CONFIGS", error);
        throw error;
    }
}


export {
    getAllWorksConfig,
    getWorkConfigById,
    searchWorkConfig,
    createWorkConfig,
    updateWorkConfig,
    deleteWorkConfig,
    deleteListWorkConfigs
}