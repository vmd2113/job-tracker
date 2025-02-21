import React, {useState, useRef} from 'react';
import {Upload, X, FileText} from 'lucide-react';
import PropTypes from 'prop-types';

const ALLOWED_FILE_TYPES = [
    '.doc', '.docx', '.pdf', '.xls', '.xlsx', '.pptx', '.ppt',
    '.csv', '.txt', '.rar', '.7z', '.jpg', '.gif', '.png',
    '.bmp', '.sql', '.vsd', '.vsdx'
];

const FileUpload = ({
                        value,
                        onChange,
                        error,
                        className = '',
                        required = false,
                        label = 'File đính kèm',
                        multiple = false
                    }) => {
    const [tooltip, setTooltip] = useState(false);
    const fileInputRef = useRef(null);

    const allowedTypesText = "Chỉ chấp nhận file " + ALLOWED_FILE_TYPES.join(', ');

    const handleFileChange = (event) => {
        const files = Array.from(event.target.files);
        const invalidFiles = files.filter(file => {
            const extension = '.' + file.name.split('.').pop().toLowerCase();
            return !ALLOWED_FILE_TYPES.includes(extension);
        });

        if (invalidFiles.length > 0) {
            const invalidFileNames = invalidFiles.map(f => f.name).join(', ');
            alert(`Các file không được hỗ trợ: ${invalidFileNames}\n${allowedTypesText}`);
            event.target.value = '';
            return;
        }

        if (onChange) {
            onChange(multiple ? files : files[0]);
        }
    };

    const handleRemoveFile = () => {
        if (fileInputRef.current) {
            fileInputRef.current.value = '';
        }
        if (onChange) {
            onChange(multiple ? [] : null);
        }
    };

    const getFileNames = () => {
        if (!value) return '';
        if (multiple && Array.isArray(value)) {
            return value.map(file => file.name).join(', ');
        }
        return value.name;
    };

    return (
        <div className={`space-y-2 ${className}`}>
            {label && (
                <label className="block text-sm font-medium text-gray-700">
                    {label}
                    {required && <span className="text-red-500 ml-1">*</span>}
                </label>
            )}

            <div
                className="relative"
                onMouseEnter={() => setTooltip(true)}
                onMouseLeave={() => setTooltip(false)}
            >
                <div className="flex items-center gap-2">
                    <button
                        type="button"
                        onClick={() => fileInputRef.current?.click()}
                        className="flex items-center px-4 py-2 bg-white border border-gray-300 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        <Upload className="w-5 h-5 mr-2"/>
                        Tải lên
                    </button>

                    {value && (
                        <button
                            type="button"
                            onClick={handleRemoveFile}
                            className="p-1 text-gray-500 hover:text-red-500"
                        >
                            <X className="w-5 h-5"/>
                        </button>
                    )}
                </div>

                <input
                    ref={fileInputRef}
                    type="file"
                    onChange={handleFileChange}
                    className="hidden"
                    multiple={multiple}
                    accept={ALLOWED_FILE_TYPES.join(',')}
                />

                {tooltip && (
                    <div className="absolute z-10 p-2 mt-2 text-sm text-white bg-gray-800 rounded-md">
                        {allowedTypesText}
                    </div>
                )}
            </div>

            {value && (
                <div className="flex items-center gap-2 p-3 bg-gray-50 rounded-md">
                    <FileText className="w-5 h-5 text-gray-500"/>
                    <span className="text-sm text-gray-600 truncate">
            {getFileNames()}
          </span>
                </div>
            )}

            {error && (
                <p className="mt-1 text-sm text-red-600">{error}</p>
            )}
        </div>
    );
};

FileUpload.propTypes = {
    value: PropTypes.oneOfType([
        PropTypes.object,
        PropTypes.arrayOf(PropTypes.object)
    ]),
    onChange: PropTypes.func,
    error: PropTypes.string,
    className: PropTypes.string,
    required: PropTypes.bool,
    label: PropTypes.string,
    multiple: PropTypes.bool
};

export default FileUpload;