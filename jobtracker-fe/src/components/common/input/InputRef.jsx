import React from 'react';
import {AlertCircle, Eye, EyeOff} from 'lucide-react';
import PropTypes from 'prop-types';

const SIZE_STYLES = {
    sm: 'px-2 py-1 text-sm',
    md: 'px-3 py-2',
    lg: 'px-4 py-3 text-lg'
};

const InputRef = React.forwardRef(({
                                       // Base props
                                       type = 'text',
                                       label,
                                       name,
                                       value,
                                       onChange,
                                       onBlur,
                                       placeholder,
                                       // Styling props
                                       size = 'md',
                                       className = '',
                                       inputClassName = '',
                                       labelClassName = '',
                                       // Validation props
                                       error,
                                       required = false,
                                       disabled = false,
                                       // Password specific props
                                       showPasswordToggle = true,
                                       // Additional props
                                       helperText,
                                       icon: Icon,
                                       ...props
                                   }, ref) => {
    const [showPassword, setShowPassword] = React.useState(false);
    const inputType = type === 'password' && showPassword ? 'text' : type;
    const isTextarea = type === 'textarea';

    const baseInputStyles = `
    w-full
    rounded-md
    border
    transition-colors
    duration-200
    disabled:bg-gray-100
    disabled:cursor-not-allowed
    focus:outline-none
    focus:ring-2
    focus:ring-blue-500
    ${error ? 'border-red-500' : 'border-gray-300'}
    ${SIZE_STYLES[size]}
  `;

    const InputComponent = isTextarea ? 'textarea' : 'input';

    return (
        <div className={`space-y-1 ${className}`}>
            {label && (
                <label
                    htmlFor={name}
                    className={`block text-sm font-medium space-y-1 text-gray-700 ${labelClassName}`}
                >
                    {label}
                    {required && <span className="text-red-500 ml-1">*</span>}
                </label>
            )}

            <div className="relative">
                {Icon && (
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        <Icon className="h-5 w-5 text-gray-400"/>
                    </div>
                )}

                <InputComponent
                    ref={ref}
                    id={name}
                    name={name}
                    type={inputType}
                    value={value}
                    onChange={onChange}
                    onBlur={onBlur}
                    placeholder={placeholder}
                    disabled={disabled}
                    required={required}
                    className={`
                        ${baseInputStyles}
                        ${Icon ? 'pl-10' : ''}
                        ${type === 'password' ? 'pr-10' : ''}
                        ${inputClassName}
                    `}
                    rows={isTextarea ? 4 : undefined}
                    {...props}
                />

                {type === 'password' && showPasswordToggle && (
                    <button
                        type="button"
                        className="absolute inset-y-0 right-0 pr-3 flex items-center"
                        onClick={() => setShowPassword(!showPassword)}
                    >
                        {showPassword ? (
                            <EyeOff className="h-5 w-5 text-gray-400"/>
                        ) : (
                            <Eye className="h-5 w-5 text-gray-400"/>
                        )}
                    </button>
                )}
            </div>

            {error && (
                <div className="flex items-center space-x-1 text-red-500 text-sm mt-1">
                    <AlertCircle className="h-4 w-4"/>
                    <span>{error}</span>
                </div>
            )}

            {helperText && !error && (
                <p className="text-sm text-gray-500 mt-1">{helperText}</p>
            )}
        </div>
    );
});

InputRef.displayName = 'InputRef';

InputRef.propTypes = {
    type: PropTypes.oneOf(['text', 'password', 'email', 'number', 'tel', 'date', 'time', 'textarea']),
    label: PropTypes.string,
    name: PropTypes.string.isRequired,
    value: PropTypes.string,
    onChange: PropTypes.func,
    onBlur: PropTypes.func,
    placeholder: PropTypes.string,
    size: PropTypes.oneOf(['sm', 'md', 'lg']),
    className: PropTypes.string,
    inputClassName: PropTypes.string,
    labelClassName: PropTypes.string,
    error: PropTypes.string,
    required: PropTypes.bool,
    disabled: PropTypes.bool,
    showPasswordToggle: PropTypes.bool,
    helperText: PropTypes.string,
    icon: PropTypes.elementType
};

export default InputRef;