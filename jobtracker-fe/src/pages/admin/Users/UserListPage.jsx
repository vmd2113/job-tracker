import React, {useState, useEffect} from 'react';
import {useToast} from "../../../components/common/toast/ToastContainer.jsx";
import useUserManagement from "../../../hooks/users/useUserManagement.jsx";
import Button from "../../../components/common/button/Button.jsx";
import LoadingSpinner from "../../../components/common/loading/LoadingSpinner.jsx";
import UserList from "../../../components/users/UserList.jsx";
import UserModal from "../../../components/users/UserModal.jsx";
import Pagination from "../../../components/common/paging/Pagination.jsx";
import UserSearch from "../../../components/users/UserSearch.jsx";


const UserListPage = () => {
    const {showToast} = useToast();
    const {
        users,
        loading,
        error,
        totalItems,
        pagination,
        handlePageChange,
        handlePageSizeChange,
        createUser,
        updateUser,
        deleteUser,
        deleteListUsers,
        searchCriteria,
        handleSearch
    } = useUserManagement();

    const [selectedIds, setSelectedIds] = useState(new Set());
    const [modalState, setModalState] = useState({
        isOpen: false,
        type: null,
        selectedUser: null
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
        // 3. Có users array (đã được khởi tạo)
        // 4. users array rỗng
        if (hasSearched && !loading && Array.isArray(users) && users.length === 0) {
            showToast("Không tìm thấy bản ghi nào", "warning");
        }
    }, [users, loading, hasSearched, showToast]);

    // Reset hasSearched khi searchCriteria thay đổi
    useEffect(() => {
        if (!searchCriteria || Object.values(searchCriteria).every(value => !value)) {
            setHasSearched(false);
        }
    }, [searchCriteria]);


    const handleSelectAll = (userIds, checked) => {
        const newSelectedIds = new Set(selectedIds);
        if (checked) {
            userIds.forEach((id) => newSelectedIds.add(id));
        } else {
            userIds.forEach((id) => newSelectedIds.delete(id));
        }
        setSelectedIds(newSelectedIds);
    };

    const handleSelectOne = (id, checked) => {
        const newIds = new Set(selectedIds);
        checked ? newIds.add(id) : newIds.delete(id);
        setSelectedIds(newIds);
    };

    const handleModalOpen = (type, user = null) => {
        setModalState({isOpen: true, type, selectedUser: user});
    };

    const handleModalClose = () => {
        setModalState({isOpen: false, type: null, selectedUser: null});
    };

    const handleModalConfirm = async (data) => {
        try {
            switch (modalState.type) {
                case 'add':
                    await createUser(data);
                    showToast('Thêm người dùng thành công', 'success');
                    break;

                case 'edit':
                    await updateUser(modalState.selectedUser.userId, data);
                    showToast('Cập nhật người dùng thành công', 'success');
                    break;

                case 'delete':
                    const idsToDelete = modalState.selectedUser
                        ? [modalState.selectedUser.userId]  // Trường hợp xóa 1 user
                        : Array.from(selectedIds);          // Trường hợp xóa nhiều user

                    console.log("IDS TO DELETE AT USER LIST PAGE", idsToDelete);
                    if (idsToDelete.length === 1) {
                        await deleteUser(idsToDelete[0]);
                        showToast('Xóa người dùng thành công', 'success');
                    } else {
                        await deleteListUsers(idsToDelete);
                        showToast(`Xóa ${idsToDelete.length} người dùng thành công`, 'success');
                    }

                    setSelectedIds(new Set()); // Reset selected IDs
                    break;

                default:
                    throw new Error('Loại thao tác không hợp lệ');
            }
            handleModalClose();
        } catch (error) {
            console.log("ERROR USER LIST PAGE", error);


            if (error.message.includes("Phone number") ||
                error.message.includes("Username") ||
                error.message.includes("Email")) {
                let msg = "Tồn tại bản ghi trùng thông tin tên tài khoản hoặc email hoặc số điện thoại"
                showToast(msg, 'error');
            }
            else{
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
                <h2 className="text-2xl text-purple-700 font-bold">Quản Lý Người Dùng</h2>
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
                <UserSearch
                    handleSearch={handleSearchWithToast}
                    searchCriteria={searchCriteria}
                    loading={loading}
                />
            </div>

            {loading ? (
                <LoadingSpinner/>
            ) : (
                <UserList
                    users={users}
                    selectedIds={selectedIds}
                    onSelectAll={handleSelectAll}
                    onSelectOne={handleSelectOne}
                    onEdit={(user) => handleModalOpen('edit', user)}
                    onDelete={(user) => handleModalOpen('delete', user)}
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

            <UserModal
                {...modalState}
                selectedIds={selectedIds}
                onConfirm={handleModalConfirm}
                onClose={handleModalClose}
            />
        </div>
    );
};

export default UserListPage;