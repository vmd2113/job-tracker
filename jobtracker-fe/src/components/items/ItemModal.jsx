import React, {useRef} from 'react';
import Modal, {ModalFooterButtons} from "../common/modal/Modal.jsx";
import ItemForm from "./ItemForm.jsx";

const ItemModal = ({
                       isOpen,
                       type,
                       selectedItem,
                       selectedIds,
                       onConfirm,
                       onClose,
                       className
                   }) => {

        const formItemRef = useRef(null);

        const handleConfirm = (data) => {
            if (type === 'delete') {
                onConfirm();
            } else {
                formItemRef.current?.submit();
            }
        };

        if (type === 'delete') {
            const isMultiDelete = !selectedItem;
            return (
                <Modal
                    isOpen={isOpen}
                    onClose={onClose}
                    title={`Xác nhận xóa`}
                    className="w-full max-w-lg mx-auto"
                    contentClassName="px-4 sm:px-6 lg:px-8"
                    footer={
                        <ModalFooterButtons
                            onConfirm={() => handleConfirm()}
                            onCancel={onClose}
                            confirmText={`Xóa`}
                            confirmVariant="danger"
                            className="flex-col sm:flex-row gap-2 sm:gap-4"
                        />
                    }
                >
                    <p className="text-sm sm:text-base text-gray-700 break-words">
                        {isMultiDelete
                            ? `Bạn có chắc chắn muốn xóa ${selectedIds.size} hạng mục đã chọn?`
                            : `Bạn có chắc chắn muốn xóa hạng mục ${selectedItem.itemId} này?`
                        }
                    </p>
                </Modal>

            )
        }


        return (

            <Modal
                className={`w-full ${className}`}
                isOpen={isOpen}
                onClose={onClose}
                size="full"
                title={type === 'add' ? 'Thêm mới hạng mục' : `Sửa hạng mục`}
                contentClassName="px-4 sm:px-6 lg:px-8"
                overlayClassName="bg-gray-500/75 backdrop-blur-sm"
                modalClassName="w-full sm:max-w-xl md:max-w-2xl lg:max-w-4xl xl:max-w-6xl mx-auto"
                footer={
                    <ModalFooterButtons
                        onConfirm={handleConfirm}
                        onCancel={onClose}
                        confirmText={type === 'add' ? 'Thêm mới hạng mục' : `Cập nhật hạng mục`}
                        className="flex-col sm:flex-row gap-2 sm:gap-4 justify-end w-full"
                        buttonClassName="w-full sm:w-auto"
                    />
                }
            >
                <ItemForm
                    ref={formItemRef}
                    initialData={selectedItem}
                    onSubmit={onConfirm}
                    type={type}
                />

            </Modal>
        );
    }
;

export default ItemModal;