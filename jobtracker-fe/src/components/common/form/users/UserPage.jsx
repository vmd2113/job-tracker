import React, {useMemo, useState} from 'react';
import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
import * as yup from 'yup';
import Modal, {ModalFooterButtons} from "../../modal/Modal.jsx";
import Button from "../../button/Button.jsx";
import Input from "../../input/Input.jsx";
import {Table, TableHeader, TableBody, TableRow, TableCell} from "../../table/Table.jsx";
import CheckboxItem from "../../checkbox/Checkbox.jsx";
import Pagination from "../../paging/Pagination.jsx";


// Schema cho form thêm/sửa
const userSchema = yup.object({
    name: yup.string().trim().required('Tên là bắt buộc'),
    email: yup.string().trim().email('Email không hợp lệ').required('Email là bắt buộc'),
    phone: yup.string().trim()
        .required('Số điện thoại là bắt buộc')
});

const UserPage = () => {

    // State cho danh sách và phân trang
    const [users, setUsers] = useState([
        {id: '1', name: 'John Doe', email: 'john@example.com', phone: '0987654321'},
        {id: '2', name: 'Jane Doe', email: 'jane@example.com', phone: '0987654322'},
        // Thêm nhiều dữ liệu mẫu để test phân trang
        ...Array.from({length: 100}, (_, i) => ({
            id: `${i + 3}`,
            name: `User ${i + 3}`,
            email: `user${i + 3}@example.com`,
            phone: `098765432${i + 3}`
        }))
    ]);

    // State cho modal
    const [modalState, setModalState] = useState({
        isOpen: false,
        type: null,
        selectedUser: null
    });

    // State cho pagination
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    // State cho checkbox selection
    const [selectedIds, setSelectedIds] = useState(new Set());

    // React Hook Form
    const {
        register,
        handleSubmit,
        reset,
        formState: {errors, isSubmitting}
    } = useForm({
        resolver: yupResolver(userSchema)
    });


    // Tính toán dữ liệu cho trang hiện tại
    const currentUsers = useMemo(() => {
        const start = (currentPage - 1) * itemsPerPage;
        return users.slice(start, start + itemsPerPage);
    }, [users, currentPage]);

    // Tính tổng số trang
    const totalPages = Math.ceil(users.length / itemsPerPage);

    // Handlers
    const handleOpenModal = (type, user = null) => {
        setModalState({isOpen: true, type, selectedUser: user});
        if (type === 'edit' && user) {
            // Reset giá trị bằng user được chọn
            reset({
                name: user.name,
                email: user.email,
                phone: user.phone,
            });
        } else {
            // Reset với giá trị rỗng cho modal "add"
            reset({
                name: '',
                email: '',
                phone: '',
            });
        }
    };


    const handleCloseModal = () => {
        setModalState({isOpen: false, type: null, selectedUser: null});
        reset({});
    };

    const onSubmit = async (data) => {
        try {
            if (modalState.type === 'add') {
                setUsers([...users, {...data, id: Date.now().toString()}]);
            } else if (modalState.type === 'edit') {
                setUsers(users.map(user =>
                    user.id === modalState.selectedUser.id ? {...data, id: user.id} : user
                ));
            }
            handleCloseModal();
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const handleDelete = () => {
        if (modalState.type === 'delete') {
            if (modalState.selectedUser) {
                // Xóa một user
                setUsers(users.filter(user => user.id !== modalState.selectedUser.id));
            } else {
                // Xóa nhiều users được chọn
                setUsers(users.filter(user => !selectedIds.has(user.id)));
                setSelectedIds(new Set());
            }
        }
        handleCloseModal();
    };

    // Checkbox handlers
    const handleSelectAll = (_, checked) => {
        if (checked) {
            setSelectedIds(new Set(currentUsers.map(user => user.id)));
        } else {
            setSelectedIds(new Set());
        }
    };

    const handleSelectOne = (id, checked) => {
        const newSelectedIds = new Set(selectedIds);
        if (checked) {
            newSelectedIds.add(id);
        } else {
            newSelectedIds.delete(id);
        }
        setSelectedIds(newSelectedIds);
    };

    // Tính trạng thái của checkbox "Chọn tất cả"
    const isAllSelected = currentUsers.length > 0 && currentUsers.every(user => selectedIds.has(user.id));
    const isIndeterminate = !isAllSelected && currentUsers.some(user => selectedIds.has(user.id));

    // Render modals
    const renderModal = () => {
        const modalProps = {
            isOpen: modalState.isOpen,
            onClose: handleCloseModal,
            size: 'md'
        };

        if (modalState.type === 'delete') {
            const isMultiDelete = !modalState.selectedUser;
            return (
                <Modal
                    {...modalProps}
                    title="Xác nhận xóa"
                    footer={
                        <ModalFooterButtons
                            onConfirm={handleDelete}
                            onCancel={handleCloseModal}
                            confirmText="Xóa"
                            confirmVariant="danger"
                        />
                    }
                >
                    <p>
                        {isMultiDelete
                            ? `Bạn có chắc chắn muốn xóa ${selectedIds.size} người dùng đã chọn?`
                            : 'Bạn có chắc chắn muốn xóa người dùng này?'
                        }
                    </p>
                </Modal>
            );
        }

        return (
            <Modal
                {...modalProps}
                title={modalState.type === 'add' ? 'Thêm người dùng' : 'Sửa người dùng'}
                footer={
                    <ModalFooterButtons
                        onConfirm={handleSubmit(onSubmit)}
                        onCancel={handleCloseModal}
                        confirmText={modalState.type === 'add' ? 'Thêm' : 'Cập nhật'}
                        isLoading={isSubmitting}
                    />
                }
            >
                <form className="space-y-4">
                    <Input
                        label="Tên"
                        {...register('name')}
                        error={errors.name?.message}
                    />
                    <Input
                        label="Email"
                        type="email"
                        {...register('email')}
                        error={errors.email?.message}
                    />
                    <Input
                        label="Số điện thoại"
                        {...register('phone')}
                        error={errors.phone?.message}
                    />
                </form>
            </Modal>
        );
    };

    return (
        <div className="container mx-auto p-4">
            {/* Header */}
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Quản lý người dùng</h1>
                <div className="space-x-2 flex">

                    <Button
                        type="delete"
                        onClick={() => handleOpenModal('delete')}
                    />

                    <Button
                        type="add"
                        onClick={() => handleOpenModal('add')}
                    />
                </div>
            </div>

            {/* Table */}
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableCell isHeader>
                            <CheckboxItem
                                id="select-all"
                                checked={isAllSelected}
                                indeterminate={isIndeterminate}
                                onChange={handleSelectAll}
                            />
                        </TableCell>
                        <TableCell isHeader>Tên</TableCell>
                        <TableCell isHeader>Email</TableCell>
                        <TableCell isHeader>Số điện thoại</TableCell>
                        <TableCell isHeader>Thao tác</TableCell>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {currentUsers.map(user => (
                        <TableRow key={user.id}>
                            <TableCell>
                                <CheckboxItem
                                    id={user.id}
                                    checked={selectedIds.has(user.id)}
                                    onChange={handleSelectOne}
                                />
                            </TableCell>
                            <TableCell>{user.name}</TableCell>
                            <TableCell>{user.email}</TableCell>
                            <TableCell>{user.phone}</TableCell>
                            <TableCell>
                                <div className="flex space-x-2">
                                    <Button
                                        type="edit"
                                        size="sm"
                                        onClick={() => handleOpenModal('edit', user)}
                                    />
                                    <Button
                                        type="delete"
                                        size="sm"
                                        onClick={() => handleOpenModal('delete', user)}
                                    />
                                </div>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>

            {/* Pagination */}
            <div className="mt-4">
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={setCurrentPage}
                />
            </div>

            {renderModal()}
        </div>
    );
};

export default UserPage;