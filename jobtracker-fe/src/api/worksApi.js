import axiosInstance from "./axiosConfig.js";

const WORKS_API = '/wfm/works';

const getAllWorks = async () => {
    try {
        const response = await axiosInstance.get(`${WORKS_API}/`);
        return response.data;
    } catch (error) {
        throw error;
    }

};



const getWorkByWorkId = async (workId) => {
    try {
        const response = await axiosInstance.get(`${WORKS_API}/${workId}`);
        return response.data;
    } catch (error) {
        throw error;
    }
}

const createWork = async (workData) => {
    try{
        const body = {
            workCode: workData.workCode,
            workContent: workData.workContent,
            workTypeId: workData.workTypeId,
            priorityId: workData.priorityId,
            startTime: workData.startTime,
            endTime: workData.endTime,
            assignedUserId: workData.assignedUserId,
            status: workData.status
        }
        const response = await axiosInstance.post(`${WORKS_API}/`, body);
        return response.data;
    }catch (error) {
        console.log("ERROR AT CREATE WORK", error);
        throw error;
    }
}

const updateWork = async (workId, workData) => {
    try {
        const body = {
            workCode: workData.workCode,
            workContent: workData.workContent,
            workTypeId: workData.workTypeId,
            priorityId: workData.priorityId,
            startTime: workData.startTime,
            endTime: workData.endTime,
            assignedUserId: workData.assignedUserId,
            status: workData.status
        }
        const response = await axiosInstance.put(`${WORKS_API}/${workId}`, body);
        return response.data;
    } catch (error) {
        console.log("ERROR AT UPDATE WORK", error);
        throw error;
    }
}

const searchWorks = async ({
                               page = 0,
                               size = 10,
                               workCode,
                               workTypeId,
                               priorityId,
                               startTime,
                               endTime,
                               assignedUserId,
                               sortBy = 'updateDate',
                               sortDirection = 'desc'
                           }) => {
    try {
        const params = {
            page: String(page),
            size: String(size),
            ...(workCode && {workCode}),
            ...(workTypeId && {workTypeId}),
            ...(priorityId && {priorityId}),
            ...(startTime && {startTime}),
            ...(endTime && {endTime}),
            ...(assignedUserId && {assignedUserId}),
            sortBy: sortBy || 'updateDate',
            sortDirection: sortDirection || 'desc'
        };

        const response = await axiosInstance.get(`${WORKS_API}/search`, {params});
        return response.data;
    } catch (error) {
        console.log("ERROR AT SEARCH WORKS", error);
        throw error;
    }
}

// Delete work
const deleteWork = async (workId) => {
    try {
        const response = await axiosInstance.delete(`${WORKS_API}/${workId}`);
        return response.data;
    } catch (error) {
        console.log("ERROR AT DELETE WORK", error);
        throw error;
    }
}

//Delete list works
const deleteListWorks = async (workIds) => {
    try {
        const response = await axiosInstance.delete(`${WORKS_API}/delete/list`, {
            data: workIds
        });
        return response.data;
    } catch (error) {
        console.log("ERROR AT DELETE LIST WORKS", error);
        throw error;
    }
}

export {
    getAllWorks,
    getWorkByWorkId,
    searchWorks,
    createWork,
    updateWork,
    deleteWork,
    deleteListWorks
}


