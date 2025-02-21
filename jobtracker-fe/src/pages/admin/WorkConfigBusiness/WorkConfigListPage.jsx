import React, {useEffect, useState} from 'react';
import {useToast} from "../../../components/common/toast/ToastContainer.jsx";
import useWorkConfigManagement from "../../../hooks/workConfig/useWorkConfigManagement.jsx";
import LoadingSpinner from "../../../components/common/loading/LoadingSpinner.jsx";
import WorkConfigList from "../../../components/workConfig/WorkConfigList.jsx";
import WorkConfigModal from "../../../components/workConfig/WorkConfigModal.jsx";
import WorkConfigSearch from "../../../components/workConfig/WorkConfigSearch.jsx";
import Pagination from "../../../components/common/paging/Pagination.jsx";
import Button from "../../../components/common/button/Button.jsx";


const WorkConfigListPage = () => {
    const {showToast} = useToast();
    const {
        workConfigs,
        loading,
        error,
        totalItems,
        pagination,
        handlePageChange,
        handlePageSizeChange,
        createWorkConfig,
        updateWorkConfig,
        deleteWorkConfig,
        deleteListWorkConfigs,
        keySearch,
        handleSearch
    } = useWorkConfigManagement();

    const [selectedIds, setSelectedIds] = useState(new Set());
    const [modalState, setModalState] = useState({
        isOpen: false,
        type: null,
        selectedWorkConfig: null
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

    React.useEffect(() => {
        // Chỉ hiển thị toast khi:
        // 1. Đã thực hiện search (hasSearched = true)
        // 2. Không còn đang loading
        // 3. Có workConfigs array (đã được khởi tạo)
        // 4. workConfigs array rỗng
        if (hasSearched && !loading && Array.isArray(workConfigs) && workConfigs.length === 0) {
            showToast("Không tìm thấy bản ghi nào", "warning");
        }
    }, [workConfigs, loading, hasSearched, showToast]);

    // Reset hasSearched khi searchCriteria thay đổi
    useEffect(() => {
        if (!keySearch || Object.values(keySearch).every(value => !value)) {
            setHasSearched(false);
        }
    }, [keySearch]);

    const handleSelectAll = (workConfigIds, checked) => {
        const newSelectedIds = new Set(selectedIds);
        if (checked) {
            workConfigIds.forEach((id) => newSelectedIds.add(id));
        } else {
            workConfigIds.forEach((id) => newSelectedIds.delete(id));
        }
        setSelectedIds(newSelectedIds);
    };

    const handleSelectOne = (id, checked) => {
        const newIds = new Set(selectedIds);
        checked ? newIds.add(id) : newIds.delete(id);
        setSelectedIds(newIds);
    };

    const handleModalOpen = (type, workConfig = null) => {
        setModalState({isOpen: true, type, selectedWorkConfig: workConfig});
    };

    const handleModalClose = () => {
        setModalState({isOpen: false, type: null, selectedWorkConfig: null});
    };

    const handleModalConfirm = async (data) => {
        try {
            switch (modalState.type) {
                case 'add':
                    await createWorkConfig(data);
                    showToast('Thêm cấu hình thành công', 'success');
                    break;

                case 'edit':
                    await updateWorkConfig(modalState.selectedWorkConfig.workConfigId, data);
                    showToast('Cập nhật cấu hình thành công', 'success');
                    break;

                case 'delete':
                    const idsToDelete = modalState.selectedWorkConfig
                        ? [modalState.selectedWorkConfig.workConfigId]  // Trường hợp xóa 1 workConfig
                        : Array.from(selectedIds);          // Trường hợp xóa nhiều workConfig

                    console.log("IDS TO DELETE AT WORK CONFIG LIST PAGE", idsToDelete);
                    if (idsToDelete.length === 1) {
                        await deleteWorkConfig(idsToDelete[0]);
                        showToast('Xóa cấu hình thành công', 'success');
                        setSelectedIds(new Set()); // Reset selected IDs
                    } else {
                        await deleteListWorkConfigs(idsToDelete);
                        showToast(`Xóa ${idsToDelete.length} cấu hình thành công`, 'success');
                    }

                    setSelectedIds(new Set()); // Reset selected IDs
                    break;

                default:
                    throw new Error('Loại thao tác không hợp lệ');
            }
            handleModalClose();
        } catch (error) {
            console.log("ERROR WORK CONFIG LIST PAGE", error);


            if (error.message.includes("Work Config Name") ||
                error.message.includes("Work Config Code")) {
                let msg = "Tồn tại bản ghi trùng thông tin tên cấu hình hoặc mã cấu hình"
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
                <h2 className="text-2xl text-purple-700 font-bold">Quản Lý Cấu Hình</h2>
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
                <WorkConfigSearch
                    handleSearch={handleSearchWithToast}
                    keySearch={keySearch}
                    loading={loading}
                />
            </div>
            {loading ? (
                <LoadingSpinner/>
            ) : (
                <WorkConfigList
                    workConfigs={workConfigs}
                    selectedIds={selectedIds}
                    onSelectAll={handleSelectAll}
                    onSelectOne={handleSelectOne}
                    onEdit={(workConfig) => handleModalOpen('edit', workConfig)}
                    onDelete={(workConfig) => handleModalOpen('delete', workConfig)}
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

            <WorkConfigModal
                {...modalState}
                selectedIds={selectedIds}
                onConfirm={handleModalConfirm}
                onClose={handleModalClose}
            />
        </div>
    );
};

export default WorkConfigListPage;