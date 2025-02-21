import PropTypes from "prop-types";

const Toggle = ({
                    isOn = false,
                    onChange,
                    disabled = false,
                    size = 'medium',
                    label,
                    id = 'toggle'
                }) => {
    // Xác định kích thước dựa trên prop size
    const getSizeClasses = () => {
        switch (size) {
            case 'small':
                return 'w-8 h-4';
            case 'large':
                return 'w-14 h-7';
            default:
                return 'w-11 h-6';
        }
    };

    // Xác định kích thước của nút toggle
    const getThumbSizeClasses = () => {
        switch (size) {
            case 'small':
                return 'h-3 w-3';
            case 'large':
                return 'h-6 w-6';
            default:
                return 'h-5 w-5';
        }
    };

    // Xác định vị trí translate cho từng kích thước
    const getTranslateClass = () => {
        if (!isOn) return 'translate-x-0';

        switch (size) {
            case 'small':
                return 'translate-x-4';
            case 'large':
                return 'translate-x-7';
            default:
                return 'translate-x-5';
        }
    };

    return (
        <label
            htmlFor={id}
            className="flex items-center cursor-pointer"
        >
            <div className="relative">
                <input
                    type="checkbox"
                    id={id}
                    className="sr-only"
                    checked={isOn}
                    onChange={onChange}
                    disabled={disabled}
                />

                <div
                    className={`
                        ${getSizeClasses()}
                        ${isOn ? 'bg-purple-700' : 'bg-gray-200'}
                        ${disabled ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'}
                        rounded-full
                        transition-colors
                        duration-200
                        ease-in-out
                        relative
                    `}
                >
                    <div
                        className={`
                            ${getThumbSizeClasses()}
                            ${getTranslateClass()}
                            absolute
                            left-0.5
                            top-1/2
                            -translate-y-1/2
                            bg-white
                            rounded-full
                            shadow-md
                            transition-transform
                            duration-200
                            ease-in-out
                        `}
                    />
                </div>
            </div>

            {label && (
                <span className={`ml-3 ${disabled ? 'opacity-50' : ''}`}>
                    {label}
                </span>
            )}
        </label>
    );
};

Toggle.propTypes = {
    isOn: PropTypes.bool,
    onChange: PropTypes.func.isRequired,
    disabled: PropTypes.bool,
    size: PropTypes.oneOf(['small', 'medium', 'large']),
    label: PropTypes.string,
    id: PropTypes.string
};

Toggle.defaultProps = {
    isOn: false,
    disabled: false,
    size: 'medium',
    id: 'toggle'
};

export default Toggle;