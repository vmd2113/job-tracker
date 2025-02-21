import React, {useEffect, useState} from 'react';
import {useToast} from "../../../components/common/toast/ToastContainer.jsx";
import useCategoryManagement from "../../../hooks/category/useCategoryManagement.jsx";
import LoadingSpinner from "../../../components/common/loading/LoadingSpinner.jsx";
import CategoryList from "../../../components/category/CategoryList.jsx";
import CategorySearch from "../../../components/category/CategorySearch.jsx";
import Button from "../../../components/common/button/Button.jsx";
import CategoryModal from "../../../components/category/CategoryModal.jsx";
import Pagination from "../../../components/common/paging/Pagination.jsx";

const CategoryListPage = () => {
    const {showToast} = useToast();
    const {
        categories,
        loading,
        error,
        totalItems,
        pagination,
        handlePageChange,
        handlePageSizeChange,
        createCategory,
        updateCategory,
        deleteCategory,
        deleteListCategories,
        keySearch,
        handleSearch
    } = useCategoryManagement();

    const [selectedIds, setSelectedIds] = useState(new Set());
    const [modalState, setModalState] = useState({
        isOpen: false,
        type: null,
        selectedCategory: null
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
        // 3. Có categories array (đã được khởi tạo)
        // 4. categories array rỗng
        if (hasSearched && !loading && Array.isArray(categories) && categories.length === 0) {
            showToast("Không tìm thấy bản ghi nào", "warning");
        }
    }, [categories, loading, hasSearched, showToast]);

    // Reset hasSearched khi searchCriteria thay đổi
    useEffect(() => {
        if (!keySearch || Object.values(keySearch).every(value => !value)) {
            setHasSearched(false);
        }
    }, [keySearch]);


    const handleSelectAll = (categoryIds, checked) => {
        const newSelectedIds = new Set(selectedIds);
        if (checked) {
            categoryIds.forEach((id) => newSelectedIds.add(id));
        } else {
            categoryIds.forEach((id) => newSelectedIds.delete(id));
        }
        setSelectedIds(newSelectedIds);
    }

    const handleSelectOne = (id, checked) => {
        const newIds = new Set(selectedIds);
        checked ? newIds.add(id) : newIds.delete(id);
        setSelectedIds(newIds);
    };

    const handleModalOpen = (type, category = null) => {
        setModalState({isOpen: true, type, selectedCategory: category});
    };

    const handleModalClose = () => {
        setModalState({isOpen: false, type: null, selectedCategory: null});
    };

    const handleModalConfirm = async (data) => {
        try {
            switch (modalState.type) {
                case 'add':
                    await createCategory(data);
                    showToast('Thêm danh mục thành công', 'success');
                    break;

                case 'edit':
                    await updateCategory(modalState.selectedCategory.categoryId, data);
                    showToast('Cập nhật danh mục thành công', 'success');
                    break;

                case 'delete':
                    const idsToDelete = modalState.selectedCategory
                        ? [modalState.selectedCategory.categoryId]  // Trường hợp xóa 1 category
                        : Array.from(selectedIds);          // Trường hợp xóa nhiều category

                    console.log("IDS TO DELETE AT CATEGORY LIST PAGE", idsToDelete);
                    if (idsToDelete.length === 1) {
                        await deleteCategory(idsToDelete[0]);
                        showToast('Xóa danh mục thành công', 'success');
                    } else {
                        await deleteListCategories(idsToDelete);
                        showToast(`Xóa ${idsToDelete.length} danh mục thành công`, 'success');
                    }

                    setSelectedIds(new Set()); // Reset selected IDs
                    break;

                default:
                    throw new Error('Loại thao tác không hợp lệ');
            }
            handleModalClose();
        } catch (error) {
            console.log("ERROR CATEGORY LIST PAGE", error);


            if (error.message.includes("Category Name") ||
                error.message.includes("Category Code")) {
                let msg = "Tồn tại bản ghi trùng thông tin tên danh mục hoặc mã danh mục"
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
                <h2 className="text-2xl text-purple-700 font-bold">Quản Lý Danh Mục</h2>
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
                <CategorySearch
                    handleSearch={handleSearchWithToast}
                    keySearch={keySearch}
                    loading={loading}
                />
            </div>
            {loading ? (
                <LoadingSpinner/>
            ) : (
                <CategoryList
                    categories={categories}
                    selectedIds={selectedIds}
                    onSelectAll={handleSelectAll}
                    onSelectOne={handleSelectOne}
                    onEdit={(category) => handleModalOpen('edit', category)}
                    onDelete={(category) => handleModalOpen('delete', category)}
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

            <CategoryModal
                {...modalState}
                selectedIds={selectedIds}
                onConfirm={handleModalConfirm}
                onClose={handleModalClose}
            />
        </div>

    );
};

export default CategoryListPage;