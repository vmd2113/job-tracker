import React from 'react';
import {X} from 'lucide-react';
import PropTypes from 'prop-types';

const Modal = ({
                   isOpen,
                   onClose,
                   title,
                   children,
                   footer,
                   size = 'lg',
                   showCloseButton = true,
                   closeOnBackdrop = true,
                   icon: Icon,
                   iconClassName,
                   className = ''
               }) => {
    // Handle ESC key press
    React.useEffect(() => {
        const handleEsc = (event) => {
            if (event.keyCode === 27) onClose();
        };
        window.addEventListener('keydown', handleEsc);
        return () => window.removeEventListener('keydown', handleEsc);
    }, [onClose]);

    if (!isOpen) return null;

    // Size variants với các breakpoint responsive
    const sizeClasses = {
        sm: 'w-full sm:max-w-[440px]',
        md: 'w-full sm:max-w-[540px]',
        lg: 'w-full sm:max-w-[640px]',
        xl: 'w-full sm:max-w-[768px]',
        full: 'w-full mx-4 sm:max-w-[calc(100%-2rem)]'
    };

    return (
        <div
            className="fixed inset-0 z-50"
            aria-labelledby="modal-title"
            role="dialog"
            aria-modal="true"
        >
            {/* Backdrop với animation */}
            <div
                className="fixed inset-0 bg-gray-500/75 transition-opacity duration-300 ease-in-out"
                aria-hidden="true"
                onClick={() => closeOnBackdrop && onClose()}
            />

            {/* Modal Container với scroll handling tốt hơn */}
            <div className="fixed inset-0 z-50 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4 text-center sm:p-0">
                    {/* Modal Content với animation và responsive padding */}
                    <div
                        className={`
              relative transform overflow-hidden rounded-lg bg-white text-left shadow-xl
              transition-all duration-300 ease-in-out
              ${sizeClasses[size]}
              ${className}
            `}
                    >
                        {/* Close Button với vị trí responsive */}
                        {showCloseButton && (
                            <button
                                onClick={onClose}
                                className="absolute right-2 top-2 sm:right-4 sm:top-4 text-gray-400
                         hover:text-gray-500 focus:outline-none transition-colors duration-200"
                            >
                                <X className="h-4 w-4 sm:h-5 sm:w-5"/>
                            </button>
                        )}

                        {/* Header với padding và spacing responsive */}
                        {(title || Icon) && (
                            <div className="bg-white px-4 pb-4 pt-5 sm:p-6 sm:pb-4">
                                <div className="flex flex-col sm:flex-row sm:items-start">
                                    {Icon && (
                                        <div
                                            className={`
                        mx-auto flex size-10 sm:size-12 shrink-0 items-center 
                        justify-center rounded-full mb-4 sm:mb-0 sm:mx-0
                        ${iconClassName || 'bg-blue-100'}
                      `}
                                        >
                                            <Icon
                                                className={`size-5 sm:size-6 ${iconClassName ? '' : 'text-blue-600'}`}/>
                                        </div>
                                    )}
                                    <div
                                        className={`
                      text-center sm:text-left
                      ${Icon ? 'sm:ml-4' : ''}
                    `}
                                    >
                                        {title && (
                                            <h3 className="text-lg sm:text-xl font-semibold text-gray-900">
                                                {title}
                                            </h3>
                                        )}
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* Body với padding responsive */}
                        <div className="bg-white px-4 py-4 sm:p-6">
                            {children}
                        </div>

                        {/* Footer với layout responsive */}
                        {footer && (
                            <div className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
                                {footer}
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

// Predefined Footer Buttons với responsive styling
export const ModalFooterButtons = ({
                                       onConfirm,
                                       onCancel,
                                       confirmText = 'Xác nhận',
                                       cancelText = 'Hủy',
                                       confirmVariant = 'primary',
                                       isLoading = false,
                                       disabled = false
                                   }) => {
    const variants = {
        primary: 'bg-blue-600 hover:bg-blue-500 text-white',
        danger: 'bg-red-600 hover:bg-red-500 text-white',
        success: 'bg-green-600 hover:bg-green-500 text-white'
    };

    return (
        <div className="flex flex-col-reverse sm:flex-row sm:justify-end w-full gap-2 sm:gap-3">
            <button
                type="button"
                className="w-full sm:w-auto inline-flex justify-center rounded-md px-3 py-2
                 text-sm font-semibold text-gray-900 bg-white shadow-sm
                 ring-1 ring-inset ring-gray-300 hover:bg-gray-50
                 transition-colors duration-200"
                onClick={onCancel}
            >
                {cancelText}
            </button>
            <button
                type="button"
                disabled={disabled || isLoading}
                className={`
          w-full sm:w-auto inline-flex justify-center rounded-md px-3 py-2 
          text-sm font-semibold shadow-sm transition-colors duration-200
          ${variants[confirmVariant]}
          disabled:opacity-50 disabled:cursor-not-allowed
        `}
                onClick={onConfirm}
            >
                {confirmText}
            </button>
        </div>
    );
};

Modal.propTypes = {
    isOpen: PropTypes.bool,
    onClose: PropTypes.func,
    title: PropTypes.string,
    children: PropTypes.node,
    footer: PropTypes.node,
    size: PropTypes.oneOf(['sm', 'md', 'lg', 'xl', 'full']),
    showCloseButton: PropTypes.bool,
    closeOnBackdrop: PropTypes.bool,
    icon: PropTypes.elementType,
    iconClassName: PropTypes.string,
    className: PropTypes.string
};

Modal.defaultProps = {
    isOpen: false,
    onClose: () => {
    },
    title: '',
    children: null,
    footer: null,
    size: 'md',
    showCloseButton: true,
    closeOnBackdrop: true,
    icon: null,
    iconClassName: '',
    className: ''
};

export default Modal;