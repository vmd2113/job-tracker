import React from 'react';
import Modal, {ModalFooterButtons} from "../common/modal/Modal.jsx";
import WorkTypeForm from "./WorkTypeForm.jsx";

const WorkTypeModal = ({
                           isOpen,
                           type,
                           selectedWorkType,
                           selectedIds,
                           onConfirm,
                           onClose,
                           className
                       }) => {

    const formWorkTypeRef = React.useRef(null);

    const handleConfirm = (data) => {
        if (type === 'delete') {
            onConfirm();
        } else {
            formWorkTypeRef.current?.submit();
        }
    };

    if (type === 'delete') {
        const isMultiDelete = !selectedWorkType;
        return (
            <Modal
                isOpen={isOpen}
                onClose={onClose}
                title="Xác nhận xóa"
                className="w-full max-w-lg mx-auto"
                contentClassName="px-4 sm:px-6 lg:px-8 py-4 sm:py-6"
                footer={
                    <ModalFooterButtons
                        onConfirm={() => handleConfirm()}
                        onCancel={onClose}
                        confirmText="Xóa"
                        confirmVariant="danger"
                        className="flex-col sm:flex-row gap-2 sm:gap-4"
                    />
                }
            >
                <p className="text-sm sm:text-base text-gray-700 break-words">
                    {isMultiDelete
                        ? `Bạn có chắc chắn muốn xóa ${selectedIds.size} loại công việc đã chọn?`
                        : `Bạn có chắc chắn muốn xóa cấu hình làm việc ${selectedWorkType.workTypeId} này?`
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
            title={type === 'add' ? 'Thêm loại làm việc' : 'Sửa loại làm việc'}
            contentClassName="px-4 sm:px-6 lg:px-8 py-4 sm:py-6"
            overlayClassName="bg-gray-500/75 backdrop-blur-sm"
            modalClassName="w-full sm:max-w-xl md:max-w-2xl lg:max-w-4xl xl:max-w-6xl mx-auto"
            footer={
                <ModalFooterButtons
                    onConfirm={handleConfirm}
                    onCancel={onClose}
                    confirmText={type === 'add' ? 'Thêm' : 'Cập nhật'}
                    className="flex-col sm:flex-row gap-2 sm:gap-4 justify-end w-full"
                    buttonClassName="w-full sm:w-auto"
                />
            }
        >
            <div className="max-h-[calc(100vh-12rem)] overflow-y-auto">
                <WorkTypeForm
                    ref={formWorkTypeRef}
                    initialData={selectedWorkType}
                    onSubmit={onConfirm}
                    type={type}
                />
            </div>
        </Modal>
    );
};

export default WorkTypeModal;