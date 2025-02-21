import React, {useEffect, useState} from 'react';
import useWorkManagement from "../../../hooks/works/useWorkManagement.jsx";
import {useToast} from "../../../components/common/toast/ToastContainer.jsx";
import WorkModal from "../../../components/works/WorkModal.jsx";
import Pagination from "../../../components/common/paging/Pagination.jsx";
import LoadingSpinner from "../../../components/common/loading/LoadingSpinner.jsx";
import WorkList from "../../../components/works/WorkList.jsx";
import * as PropTypes from "prop-types";
import Button from "../../../components/common/button/Button.jsx";
import WorkSearch from "../../../components/works/WorkSearch.jsx";


const WorkListPage = () => {
    const {showToast} = useToast();
    const {
        works,
        loading,
        error,
        totalItems,
        pagination,
        handlePageChange,
        handlePageSizeChange,
        createWork,
        updateWork,
        deleteWork,
        deleteListWorks,
        keySearch,
        handleSearch
    } = useWorkManagement();

    const [selectedIds, setSelectedIds] = useState(new Set());
    const [modalState, setModalState] = useState({
        isOpen: false,
        type: null,
        selectedWork: null
    });

    // Thêm state để theo dõi việc đã thực hiện search hay chưa
    const [hasSearched, setHasSearched] = useState(false);
    const [searching, setSearching] = useState(false);


    const handleSearchWithToast = async (criteria) => {
        try {
            setSearching(true);
            setHasSearched(true);
            await handleSearch(criteria);
        } catch (searchError) {
            console.error('Error in handleSearchWithToast:', error);
            showToast(error.message, 'error');
        } finally {
            setSearching(false);
        }
    };

    useEffect(() => {
        // Chỉ hiển thị toast khi:
        // 1. Đã thực hiện search (hasSearched = true)
        // 2. Không còn đang loading
        // 3. Có works array (đã được khởi tạo)
        // 4. works array rỗng
        if (hasSearched && !loading && Array.isArray(works) && works.length === 0) {
            showToast("Không tìm thấy bản ghi nào", "warning");
        }
    }, [works, loading, hasSearched, showToast]);

    // Reset hasSearched khi searchCriteria thay đổi
    useEffect(() => {
        if (!keySearch || Object.values(keySearch).every(value => !value)) {
            setHasSearched(false);
        }
    }, [keySearch]);


    const handleSelectAll = (workIds, checked) => {
        const newSelectedIds = new Set(selectedIds);
        if (checked) {
            workIds.forEach((id) => newSelectedIds.add(id));
        } else {
            workIds.forEach((id) => newSelectedIds.delete(id));
        }
        setSelectedIds(newSelectedIds);
    };

    const handleSelectOne = (id, checked) => {
        const newIds = new Set(selectedIds);
        checked ? newIds.add(id) : newIds.delete(id);
        setSelectedIds(newIds);
    };

    const handleModalOpen = (type, work = null) => {
        setModalState({isOpen: true, type, selectedWork: work});
    };

    const handleModalClose = () => {
        setModalState({isOpen: false, type: null, selectedWork: null});
    };

    const handleModalConfirm = async (data) => {
        try {
            switch (modalState.type) {
                case 'add':
                    await createWork(data);
                    showToast('Thêm công việc thành công', 'success');
                    break;

                case 'edit':
                    await updateWork(modalState.selectedWork.worksId, data);
                    showToast('Cập nhật công việc thành công', 'success');
                    break;

                case 'delete':
                    const idsToDelete = modalState.selectedWork
                        ? [modalState.selectedWork.worksId]  // Trường hợp xóa 1 work
                        : Array.from(selectedIds);          // Trường hợp xóa nhiều work

                    console.log("IDS TO DELETE AT WORK LIST PAGE", idsToDelete);
                    if (idsToDelete.length === 1) {
                        await deleteWork(idsToDelete[0]);
                        showToast('Xóa công việc thành công', 'success');
                    } else {
                        await deleteListWorks(idsToDelete);
                        showToast(`Xóa ${idsToDelete.length} công việc thành công`, 'success');
                    }

                    setSelectedIds(new Set()); // Reset selected IDs
                    break;

                default:
                    throw new Error('Loại thao tác không hợp lệ');
            }
            handleModalClose();
        } catch (error) {
            console.log("ERROR WORK LIST PAGE", error);


            if (error.message.includes("Work Name") ||
                error.message.includes("Work Code")) {
                let msg = "Tồn tại bản ghi trùng thông tin tên công việc hoặc mã công việc"
                showToast(msg, 'error');
            } else {
                showToast(error.message, 'error')
            }

        }
    };

    const handleSizeChange = (newSize) => {
        handlePageSizeChange(newSize);
        setSelectedIds(new Set());
    };
    return (
        <div className="container mx-auto p-2">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl text-purple-700 font-bold">Quản Lý Công Việc</h2>
                <div className="space-x-2 flex items-center">
                    <Button
                        type="add"
                        onClick={() => handleModalOpen('add')}
                    />
                    <Button
                        type="delete"
                        className="disabled:opacity-65"
                        disabled={selectedIds.size < 1}
                        onClick={() => handleModalOpen('delete')}
                    />
                </div>
            </div>
            <div className="w-full my-5">

                <WorkSearch
                    handleSearch={handleSearchWithToast}
                    keySearch={keySearch}
                    loading={loading}
                />

            </div>
            {loading ? (
                <LoadingSpinner/>
            ) : (
                <WorkList
                    works={works}
                    selectedIds={selectedIds}
                    onSelectAll={handleSelectAll}
                    onSelectOne={handleSelectOne}
                    onEdit={(work) => handleModalOpen('edit', work)}
                    onDelete={(work) => handleModalOpen('delete', work)}
                />
            )}
            <div className="flex items-center justify-between mt-4 border-none outline-none">
                <select
                    value={pagination.pageSize}
                    onChange={(e) => handleSizeChange(Number(e.target.value))}
                    className="rounded p-2 outline-none border-none bg-purple-100 text-gray-800 hover:bg-purple-200"
                >
                    <option value="5">5 per page</option>
                    <option value="10">10 per page</option>
                    <option value="20">20 per page</option>
                </select>

                <Pagination
                    currentPage={pagination.currentPage}
                    totalItems={totalItems}
                    pageSize={pagination.pageSize}
                    onPageChange={handlePageChange}
                    className="ml-4"
                />
            </div>

            <WorkModal
                {...modalState}
                selectedIds={selectedIds}
                onConfirm={handleModalConfirm}
                onClose={handleModalClose}
            /></div>
    );
};

export default WorkListPage;