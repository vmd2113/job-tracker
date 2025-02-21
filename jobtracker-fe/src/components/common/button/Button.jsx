import PropTypes from 'prop-types';
import {Tooltip} from 'react-tooltip';
import {
    Plus,
    Pencil,
    Trash2,
    Search,
    Check,
    ArrowLeft,
    X,
    Loader2,
} from 'lucide-react';

const VARIANT_STYLES = {
    primary: 'rounded-2xl bg-purple-600 hover:bg-purple-700 text-white',
    secondary: 'bg-gray-600 hover:bg-gray-700 text-white',
    success: 'bg-green-600 hover:bg-green-700 text-white',
    danger: 'bg-red-600 hover:bg-red-700 text-white',
    warning: 'bg-yellow-600 hover:bg-yellow-700 text-white',
    outline: 'border-2 border-gray-300 hover:bg-gray-100'
};

const SIZE_STYLES = {
    sm: 'px-2 py-1 text-sm',
    md: 'px-4 py-2',
    lg: 'px-6 py-3 text-lg'
};

const BUTTON_TYPES = {
    add: {icon: Plus, variant: 'primary', label: 'Thêm', tooltip: 'Thêm mới'},
    edit: {icon: Pencil, variant: 'warning', label: 'Sửa', tooltip: 'Chỉnh sửa'},
    delete: {icon: Trash2, variant: 'danger', label: 'Xóa', tooltip: 'Xóa'},
    search: {icon: Search, variant: 'secondary', label: 'Tìm kiếm', tooltip: 'Tìm kiếm'},
    confirm: {icon: Check, variant: 'success', label: 'Xác nhận', tooltip: 'Xác nhận'},
    back: {icon: ArrowLeft, variant: 'outline', label: 'Trở về', tooltip: 'Quay lại'},
    cancel: {icon: X, variant: 'outline', label: 'Hủy', tooltip: 'Hủy bỏ'},
    reset: {variant: 'outline', label: 'Reset', tooltip: 'Đặt lại'},
    other: {variant: 'outline', label: 'Other', tooltip: ''}
};

const Button = ({
                    type,
                    variant,
                    size = 'md',
                    icon: CustomIcon,
                    label,
                    tooltip,
                    disabled = false,
                    loading = false,
                    className = '',
                    onClick,
                    alignment = 'center',
                    tooltipPlace = 'top',
                    ...props
                }) => {
    const buttonConfig = type ? BUTTON_TYPES[type] : {};
    const finalVariant = variant || buttonConfig.variant || 'primary';
    const finalLabel = label || buttonConfig.label;
    const finalTooltip = tooltip || buttonConfig.tooltip;
    const Icon = CustomIcon || (buttonConfig ? buttonConfig.icon : null);

    const baseStyles = 'flex items-center outline-none rounded-xl transition-colors duration-200 focus:outline-none disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-100';
    const alignmentStyles = {
        'center': 'justify-center',
        'start': 'justify-start',
        'end': 'justify-end',
        'between': 'justify-between',
    }[alignment];

    // Tạo unique ID cho mỗi button
    const tooltipId = `tooltip-${type || 'custom'}-${Math.random().toString(36).substr(2, 9)}`;

    return (
        <>
            <button
                className={`
                    ${baseStyles}
                    ${alignmentStyles}
                    ${VARIANT_STYLES[finalVariant]}
                    ${SIZE_STYLES[size]}
                    ${className}
                `}
                disabled={disabled || loading}
                onClick={onClick}
                data-tooltip-id={tooltipId}
                data-tooltip-content={finalTooltip}
                {...props}
            >
                {loading && (
                    <Loader2 className="w-4 h-4 mr-2 animate-spin"/>
                )}
                {Icon && !loading && (
                    <Icon className="w-4 h-4 mr-2"/>
                )}
                {finalLabel}
            </button>

            {finalTooltip && (
                <Tooltip
                    id={tooltipId}
                    place={tooltipPlace}
                    // Các options cơ bản
                    delayShow={200}
                    delayHide={100}
                    // Style cơ bản
                    className="z-50 !bg-purple-50 !text-gray-500 !px-3 !py-1 !rounded-lg !text-sm"
                />
            )}
        </>
    );
};

Button.propTypes = {
    type: PropTypes.oneOf(['add', 'edit', 'delete', 'search', 'confirm', 'back', 'cancel', 'reset', 'other']),
    variant: PropTypes.oneOf(['primary', 'secondary', 'success', 'danger', 'warning', 'outline']),
    size: PropTypes.oneOf(['sm', 'md', 'lg']),
    icon: PropTypes.elementType,
    label: PropTypes.string,
    tooltip: PropTypes.string,
    disabled: PropTypes.bool,
    loading: PropTypes.bool,
    className: PropTypes.string,
    onClick: PropTypes.func,
    alignment: PropTypes.oneOf(['center', 'start', 'end', 'between']),
    tooltipPlace: PropTypes.oneOf(['top', 'bottom', 'left', 'right']),
};

export default Button;