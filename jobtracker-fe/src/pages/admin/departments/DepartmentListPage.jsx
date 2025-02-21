import React, {useEffect, useState} from 'react';
import {useToast} from "../../../components/common/toast/ToastContainer.jsx";
import useDepartmentManagement from "../../../hooks/departments/useDepartmentManagement.jsx";
import DepartmentModal from "../../../components/departments/DepartmentModal.jsx";
import Pagination from "../../../components/common/paging/Pagination.jsx";
import DepartmentList from "../../../components/departments/DepartmentList.jsx";
import LoadingSpinner from "../../../components/common/loading/LoadingSpinner.jsx";
import DepartmentSearch from "../../../components/departments/DepartmentSearch.jsx";
import Button from "../../../components/common/button/Button.jsx";


const DepartmentListPage = () => {

    console.log("LOAD DEPARTMENT LIST PAGE");

    const {showToast} = useToast();
    const {
        departments,
        loading,
        error,
        totalItems,
        pagination,
        handlePageChange,
        handlePageSizeChange,
        createDepartment,
        updateDepartment,
        deleteDepartments,
        deleteListDepartments,
        keySearch,
        handleSearch
    } = useDepartmentManagement();

    const [selectedIds, setSelectedIds] = useState(new Set());
    const [modalState, setModalState] = useState({
        isOpen: false,
        type: null,
        selectedDepartment: null
    });

    const [hasSearched, setHasSearched] = useState(false);
    const [searching, setSearching] = useState(false);


    // Thêm state để theo dõi việc đã thực hiện search hay chưa
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
        // 3. Có departments array (đã được khởi tạo)
        // 4. departments array rỗng
        if (hasSearched && !loading && Array.isArray(departments) && departments.length === 0) {
            showToast("Không tìm thấy bản ghi nào", "warning");
        }
    }, [departments, loading, hasSearched, showToast]);

    // Reset hasSearched khi searchCriteria thay đổi
    useEffect(() => {
        if (!keySearch || Object.values(keySearch).every(value => !value)) {
            setHasSearched(false);
        }
    }, [keySearch]);

    const handleSelectAll = (departmentIds, checked) => {
        const newSelectedIds = new Set(selectedIds);
        if (checked) {
            departmentIds.forEach((id) => newSelectedIds.add(id));
        } else {
            departmentIds.forEach((id) => newSelectedIds.delete(id));
        }
        setSelectedIds(newSelectedIds);
    };

    const handleSelectOne = (id, checked) => {
        const newIds = new Set(selectedIds);
        checked ? newIds.add(id) : newIds.delete(id);
        setSelectedIds(newIds);
    };

    const handleModalOpen = (type, department = null) => {
        setModalState({isOpen: true, type, selectedDepartment: department});
        console.log("CHECK DE: ", department)
    };

    const handleModalClose = () => {
        setModalState({isOpen: false, type: null, selectedDepartment: null});
    };

    const handleModalConfirm = async (data) => {
        try {
            switch (modalState.type) {
                case 'add':
                    await createDepartment(data);
                    showToast('Thêm phòng ban thành công', 'success');
                    break;

                case 'edit':
                    await updateDepartment(modalState.selectedDepartment.departmentId, data);
                    console.log("CHECK_UPATE: ", modalState.selectedDepartment)
                    console.log("CHECK - DATA: ", data);
                    showToast('Cập nhật phòng ban thành công', 'success');
                    break;

                case 'delete':
                    const idsToDelete = modalState.selectedDepartment
                        ? [modalState.selectedDepartment.departmentId]  // Trường hợp xóa 1 department
                        : Array.from(selectedIds);          // Trường hợp xóa nhiều department

                    console.log("IDS TO DELETE AT DEPARTMENT LIST PAGE", idsToDelete);
                    if (idsToDelete.length === 1) {
                        await deleteDepartments(idsToDelete[0]);
                        showToast('Xóa phòng ban thành công', 'success');
                    } else {
                        await deleteListDepartments(idsToDelete);
                        showToast(`Xóa ${idsToDelete.length} phòng ban thành công`, 'success');
                    }

                    setSelectedIds(new Set()); // Reset selected IDs
                    break;

                default:
                    throw new Error('Loại thao tác không hợp lệ');
            }
            handleModalClose();
        } catch (error) {
            console.log("ERROR DEPARTMENT LIST PAGE", error);


            if (error.message.includes("Department Name") ||
                error.message.includes("Department Code")) {
                let msg = "Tồn tại bản ghi trùng thông tin tên phòng ban hoặc code phòng ban"
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
                <h2 className="text-2xl text-purple-700 font-bold">Quản Lý Phòng Ban</h2>
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
                <DepartmentSearch
                    handleSearch={handleSearchWithToast}
                    keySearch={keySearch}
                    loading={loading}
                />
            </div>

            {loading ? (
                <LoadingSpinner/>
            ) : (
                <DepartmentList
                    departments={departments}
                    selectedIds={selectedIds}
                    onSelectAll={handleSelectAll}
                    onSelectOne={handleSelectOne}
                    onEdit={(department) => handleModalOpen('edit', department)}
                    onDelete={(department) => handleModalOpen('delete', department)}
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

            <DepartmentModal
                {...modalState}
                selectedIds={selectedIds}
                onConfirm={handleModalConfirm}
                onClose={handleModalClose}
            />
        </div>
    );
};

export default DepartmentListPage;