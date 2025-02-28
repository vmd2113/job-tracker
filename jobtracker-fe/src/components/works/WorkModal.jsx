import React from 'react';
import WorkForm from "./WorkForm.jsx";
import Modal, {ModalFooterButtons} from "../common/modal/Modal.jsx";

const WorkModal = ({
                       isOpen,
                       type,
                       selectedWorks,
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
        const isMultiDelete = !selectedWorks;
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
                        ? `Bạn có chắc chắn muốn xóa ${selectedIds.size} công việc đã chọn?`
                        : `Bạn có chắc chắn muốn xóa cấu hình làm việc ${selectedWorks.workId} này?`
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
            title={type === 'add' ? 'Thêm việc làm' : 'Sửa việc làm'}
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
                <WorkForm
                    ref={formRef}
                    initialData={selectedWorks}
                    onSubmit={onConfirm}
                    type={type}
                />
            </div>
        </Modal>
    );
};

export default WorkModal;