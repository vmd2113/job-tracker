import React from 'react';
import {AlertCircle, Eye, EyeOff} from 'lucide-react';
import PropTypes from 'prop-types';



const SIZE_STYLES = {
    sm: 'px-2 py-1 text-sm',
    md: 'px-3 py-2',
    lg: 'px-4 py-3 text-lg'
};

const Input = ({
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
               }) => {
    const [showPassword, setShowPassword] = React.useState(false);
    const inputType = type === 'password' && showPassword ? 'text' : type;
    const isTextarea = type === 'textarea';
    const INPUT_TYPES = {
        text: {type: 'text'},
        password: {type: 'password'},
        email: {type: 'email'},
        number: {type: 'number'},
        tel: {type: 'tel'},
        date: {type: 'date'},
        time: {type: 'time'},
        textarea: {type: 'textarea'}
    };
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

    const getAutoCompleteValue = () => {
        if (type === 'password') return 'new-password';
        if (type === 'email') return 'off';
        return 'no-auto'; // Custom value to prevent autofill
    };

    return (
        <div className={`space-y- my-2 ${className}`}>
            {/* Label */}
            {label && (
                <label
                    htmlFor={name}
                    className={`block mb-2 text-sm font-medium space-y-1 text-gray-700 ${labelClassName}`}
                >
                    {label}
                    {required && <span className="text-red-500 ml-1">*</span>}
                </label>
            )}

            {/* Input wrapper */}
            <div className="relative">
                {/* Icon */}
                {Icon && (
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        <Icon className="h-5 w-5 text-gray-400"/>
                    </div>
                )}

                {/* Input/Textarea */}
                <InputComponent
                    id={name}
                    name={name}
                    type={inputType}
                    value={value}
                    onChange={onChange}
                    onBlur={onBlur}
                    placeholder={placeholder}
                    disabled={disabled}
                    required={required}
                    autoComplete={getAutoCompleteValue()}
                    className={`
            ${baseInputStyles}
            ${Icon ? 'pl-10' : ''}
            ${type === 'password' ? 'pr-10' : ''}
            ${inputClassName}
          `}
                    rows={isTextarea ? 4 : undefined}
                    {...props}
                />

                {/* Password toggle button */}
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

            {/* Error message */}
            {error && (
                <div className="flex items-center space-x-1 text-red-500 text-sm mt-1">
                    <AlertCircle className="h-4 w-4"/>
                    <span>{error}</span>
                </div>
            )}

            {/* Helper text */}
            {helperText && !error && (
                <p className="text-sm text-gray-500 mt-1">{helperText}</p>
            )}
        </div>
    );
};

Input.propTypes = {
    type: PropTypes.oneOf(['text', 'password', 'email', 'number', 'tel', 'date', 'time', 'textarea']),
    label: PropTypes.string,
    name: PropTypes.string.isRequired,
    value: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.number
    ]),
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

Input.defaultProps = {
    type: 'text',
    label: null,
    name: null,
    value: null,
    onChange: null,
    onBlur: null,
    placeholder: null,
    size: 'md',
    className: '',
    inputClassName: '',
    labelClassName: '',
    error: null,
    required: false,
    disabled: false,
    showPasswordToggle: true,
    helperText: null,
    icon: null
};

export default Input;