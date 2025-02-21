import React, {useEffect, useState} from 'react';
import {useToast} from "../../../components/common/toast/ToastContainer.jsx";
import useItemManagement from "../../../hooks/items/useItemManagement.jsx";
import LoadingSpinner from "../../../components/common/loading/LoadingSpinner.jsx";
import ItemList from "../../../components/items/ItemList.jsx";
import ItemModal from "../../../components/items/ItemModal.jsx";
import Pagination from "../../../components/common/paging/Pagination.jsx";
import ItemSearch from "../../../components/items/ItemSearch.jsx";
import Button from "../../../components/common/button/Button.jsx";



const ItemListPage = () => {

    const {showToast} = useToast();
    const {
        items,
        loading,
        error,
        totalItems,
        pagination,
        handlePageChange,
        handlePageSizeChange,
        createItem,
        updateItem,
        deleteItem,
        deleteListItems,
        keySearch,
        handleSearch
    } = useItemManagement();

    const [selectedIds, setSelectedIds] = useState(new Set());
    const [modalState, setModalState] = useState({
        isOpen: false,
        type: null,
        selectedItem: null
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
        // 3. Có items array (đã được khởi tạo)
        // 4. items array rỗng
        if (hasSearched && !loading && Array.isArray(items) && items.length === 0) {
            showToast("Không tìm thấy bản ghi nào", "warning");
        }
    }, [items, loading, hasSearched, showToast]);

    // Reset hasSearched khi searchCriteria thay đổi
    useEffect(() => {
        if (!keySearch || Object.values(keySearch).every(value => !value)) {
            setHasSearched(false);
        }
    }, [keySearch]);


    const handleSelectAll = (itemIds, checked) => {
        const newSelectedIds = new Set(selectedIds);
        if (checked) {
            itemIds.forEach((id) => newSelectedIds.add(id));
        } else {
            itemIds.forEach((id) => newSelectedIds.delete(id));
        }
        setSelectedIds(newSelectedIds);
    };

    const handleSelectOne = (id, checked) => {
        const newIds = new Set(selectedIds);
        checked ? newIds.add(id) : newIds.delete(id);
        setSelectedIds(newIds);
    };

    const handleModalOpen = (type, item = null) => {
        setModalState({isOpen: true, type, selectedItem: item});
    };

    const handleModalClose = () => {
        setModalState({isOpen: false, type: null, selectedItem: null});
    };

    const handleModalConfirm = async (data) => {
        try {
            switch (modalState.type) {
                case 'add':
                    console.log("DATA AFTER CREATE ITEM AT ITEM LIST PAGE", data);
                    await createItem(data);
                    showToast('Thêm hạng mục thành công', 'success');
                    break;

                case 'edit':
                    console.log("DATA AFTER UPDATE ITEM AT ITEM LIST PAGE", data);
                    await updateItem(modalState.selectedItem.itemId, data);
                    showToast('Cập nhật hạng mục thành công', 'success');
                    break;

                case 'delete':
                    const idsToDelete = modalState.selectedItem
                        ? [modalState.selectedItem.itemId]  // Trường hợp xóa 1 item
                        : Array.from(selectedIds);          // Trường hợp xóa nhiều item

                    console.log("IDS TO DELETE AT ITEM LIST PAGE", idsToDelete);
                    if (idsToDelete.length === 1) {
                        await deleteItem(idsToDelete[0]);
                        showToast('Xóa hạng mục thành công', 'success');
                        setSelectedIds(new Set()); // Reset selected IDs
                    } else {
                        await deleteListItems(idsToDelete);
                        showToast(`Xóa ${idsToDelete.length} hạng mục thành công`, 'success');
                    }

                    setSelectedIds(new Set()); // Reset selected IDs
                    break;

                default:
                    throw new Error('Loại thao tác không hợp lệ');
            }
            handleModalClose();
        } catch (error) {
            console.log("ERROR ITEM LIST PAGE", error);


            if (error.message.includes("Item Name") ||
                error.message.includes("Item Code")) {
                let msg = "Tồn tại bản ghi trùng thông tin tên hạng mục hoặc code hạng mục"
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
                <h2 className="text-2xl text-purple-700 font-bold">Quản Lý Hạng Mục</h2>
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
                <ItemSearch
                    handleSearch={handleSearchWithToast}
                    keySearch={keySearch}
                    loading={loading}
                />
            </div>

            {loading ? (
                <LoadingSpinner/>
            ) : (
                <ItemList
                    items={items}
                    selectedIds={selectedIds}
                    onSelectAll={handleSelectAll}
                    onSelectOne={handleSelectOne}
                    onEdit={(item) => handleModalOpen('edit', item)}
                    onDelete={(item) => handleModalOpen('delete', item)}
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

            <ItemModal
                {...modalState}
                selectedIds={selectedIds}
                onConfirm={handleModalConfirm}
                onClose={handleModalClose}
            />

        </div>
    );
};

export default ItemListPage;