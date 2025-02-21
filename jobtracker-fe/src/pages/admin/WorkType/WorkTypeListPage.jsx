import React, {useEffect, useState} from 'react';
import {useToast} from "../../../components/common/toast/ToastContainer.jsx";
import useWorkTypeManagement from "../../../hooks/workTypes/useWorkTypeManagement.jsx";
import WorkTypeList from "../../../components/workType/WorkTypeList.jsx";
import WorkTypeModal from "../../../components/workType/WorkTypeModal.jsx";
import Pagination from "../../../components/common/paging/Pagination.jsx";
import WorkTypeSearch from "../../../components/workType/WorkTypeSearch.jsx";
import Button from "../../../components/common/button/Button.jsx";
import LoadingSpinner from "../../../components/common/loading/LoadingSpinner.jsx";


const WorkTypeListPage = () => {
    const {showToast} = useToast();
    const {
        workTypes,
        loading,
        error,
        totalItems,
        pagination,
        handlePageChange,
        handlePageSizeChange,
        createWorkType,
        updateWorkType,
        deleteWorkType,
        deleteListWorkTypes,
        keySearch,
        handleSearch
    } = useWorkTypeManagement();

    const [selectedIds, setSelectedIds] = useState(new Set());
    const [modalState, setModalState] = useState({
        isOpen: false,
        type: null,
        selectedWorkType: null
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
        // 3. Có workTypes array (đã được khởi tạo)
        // 4. workTypes array rỗng
        if (hasSearched && !loading && Array.isArray(workTypes) && workTypes.length === 0) {
            showToast("Không tìm thấy bản ghi nào", "warning");
        }
    }, [workTypes, loading, hasSearched, showToast]);

    // Reset hasSearched khi searchCriteria thay đổi
    useEffect(() => {
        if (!keySearch || Object.values(keySearch).every(value => !value)) {
            setHasSearched(false);
        }
    }, [keySearch]);


    const handleSelectAll = (workTypeIds, checked) => {
        const newSelectedIds = new Set(selectedIds);
        if (checked) {
            workTypeIds.forEach((id) => newSelectedIds.add(id));
        } else {
            workTypeIds.forEach((id) => newSelectedIds.delete(id));
        }
        setSelectedIds(newSelectedIds);
    };

    const handleSelectOne = (id, checked) => {
        const newIds = new Set(selectedIds);
        checked ? newIds.add(id) : newIds.delete(id);
        setSelectedIds(newIds);
    };

    const handleModalOpen = (type, workType = null) => {
        setModalState({isOpen: true, type, selectedWorkType: workType});
    };

    const handleModalClose = () => {
        setModalState({isOpen: false, type: null, selectedWorkType: null});
    };

    const handleModalConfirm = async (data) => {
        try {
            switch (modalState.type) {
                case 'add':
                    await createWorkType(data);
                    showToast('Thêm loại công việc thành công', 'success');
                    break;

                case 'edit':
                    await updateWorkType(modalState.selectedWorkType.workTypeId, data);
                    showToast('Cập nhật loại công việc thành công', 'success');
                    break;

                case 'delete':
                    const idsToDelete = modalState.selectedWorkType
                        ? [modalState.selectedWorkType.workTypeId]  // Trường hợp xóa 1 workType
                        : Array.from(selectedIds);          // Trường hợp xóa nhiều workType

                    console.log("IDS TO DELETE AT WORK TYPE LIST PAGE", idsToDelete);
                    if (idsToDelete.length === 1) {
                        await deleteWorkType(idsToDelete[0]);
                        showToast('Xóa loại công việc thành công', 'success');
                    } else {
                        await deleteListWorkTypes(idsToDelete);
                        showToast(`Xóa ${idsToDelete.length} loại công việc thành công`, 'success');
                    }

                    setSelectedIds(new Set()); // Reset selected IDs
                    break;

                default:
                    throw new Error('Loại thao tác không hợp lệ');
            }
            handleModalClose();
        } catch (error) {
            console.log("ERROR WORK TYPE LIST PAGE", error);


            if (error.message.includes("Work Type Name") ||
                error.message.includes("Work Type Code")) {
                let msg = "Tồn tại bản ghi trùng thông tin tên loại công việc hoặc code loại công việc"
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
                <h2 className="text-2xl text-purple-700 font-bold">Quản Lý Loại Công Việc</h2>
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
                <WorkTypeSearch
                    handleSearch={handleSearchWithToast}
                    keySearch={keySearch}
                    loading={loading}
                />
            </div>
            {loading ? (
                <LoadingSpinner/>
            ) : (
                <WorkTypeList
                    workTypes={workTypes}
                    selectedIds={selectedIds}
                    onSelectAll={handleSelectAll}
                    onSelectOne={handleSelectOne}
                    onEdit={(workType) => handleModalOpen('edit', workType)}
                    onDelete={(workType) => handleModalOpen('delete', workType)}
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
            <WorkTypeModal
                {...modalState}
                selectedIds={selectedIds}
                onConfirm={handleModalConfirm}
                onClose={handleModalClose}
            />
        </div>
    );
};

export default WorkTypeListPage;