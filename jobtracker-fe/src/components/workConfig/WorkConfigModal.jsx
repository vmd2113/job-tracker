import React from 'react';
import Modal, {ModalFooterButtons} from "../common/modal/Modal.jsx";
import WorkConfigForm from "./WorkConfigForm.jsx";

const WorkConfigModal = ({
                             isOpen,
                             type,
                             selectedWorkConfig,
                             selectedIds,
                             onConfirm,
                             onClose,
                             className
                         }) => {
    const formRef = React.useRef(null);

    const handleConfirm = (data) => {
        if (type === 'delete') {
            onConfirm();
        } else {
            formRef.current?.submit();
        }
    };

    if (type === 'delete') {
        const isMultiDelete = !selectedWorkConfig;
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
                        ? `Bạn có chắc chắn muốn xóa ${selectedIds.size} cấu hình làm việc đã chọn?`
                        : `Bạn có chắc chắn muốn xóa cấu hình làm việc ${selectedWorkConfig.workConfigId} này?`
                    }
                </p>

            </Modal>
        )
    }


    return (
        <Modal
            isOpen={isOpen}
            onClose={onClose}
            title={type === 'add' ? 'Thêm cấu hình làm việc' : 'Sửa cấu hình làm việc'}
            footer={
                <ModalFooterButtons
                    onConfirm={handleConfirm}
                    onCancel={onClose}
                    confirmText={type === 'add' ? 'Thêm' : 'Cập nhật'}
                />
            }
        >
            <WorkConfigForm
                ref={formRef}
                initialData={selectedWorkConfig}
                onSubmit={onConfirm}
                type={type}
            />
        </Modal>
    );
};

export default WorkConfigModal;