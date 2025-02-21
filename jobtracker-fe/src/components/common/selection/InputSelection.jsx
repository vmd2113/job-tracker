// InputSelection.jsx
import {useEffect, useRef, useState} from "react";
import {ChevronDown, Check} from "lucide-react";

const InputSelection = ({
                            options = [],
                            value = null,
                            onChange = () => {},
                            placeholder = "Chọn",
                            disabled = false,
                            className = "",
                            ...props
                        }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [searchTerm, setSearchTerm] = useState("");
    const [filteredOptions, setFilteredOptions] = useState(options);
    const wrapperRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (wrapperRef.current && !wrapperRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    useEffect(() => {
        const filtered = options.filter((option) =>
            option.label?.toLowerCase().includes(searchTerm.toLowerCase())
        );
        setFilteredOptions(filtered);
    }, [searchTerm, options]);

    // Chỉ lấy label của option có value tương ứng
    const selectedLabel = options.find((opt) => opt.value === value)?.label || "";

    const handleSelect = (option) => {
        // Chỉ trả về value thay vì cả object option
        onChange(option.value);
        setIsOpen(false);
        setSearchTerm("");
    };

    return (
        <div ref={wrapperRef} className={`relative w-full ${className}`}>
            <div
                onClick={() => !disabled && setIsOpen(!isOpen)}
                className={`flex items-center justify-between w-full px-4 py-2 border rounded-lg cursor-pointer 
                    ${disabled ? "bg-gray-100 cursor-not-allowed" : "bg-white hover:border-purple-400"}
                    ${isOpen ? "border-purple-400" : "border-gray-300"}`}
            >
                <span className={`${!value ? "text-gray-400" : "text-gray-900"}`}>
                    {selectedLabel || placeholder}
                </span>
                <ChevronDown
                    className={`w-4 h-4 transition-transform ${isOpen ? "transform rotate-180" : ""}`}
                />
            </div>

            {isOpen && (
                <div className="absolute z-50 w-full mt-1 bg-white border border-gray-300 rounded-lg shadow-lg">
                    {options.length > 10 && (
                        <div className="p-2 border-b">
                            <input
                                type="text"
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                placeholder="Tìm kiếm..."
                                className="w-full px-3 py-1 border border-gray-300 rounded focus:outline-none focus:border-purple-400"
                            />
                        </div>
                    )}

                    <div className="max-h-60 overflow-y-auto">
                        {filteredOptions.length === 0 ? (
                            <div className="px-4 py-2 text-gray-500">Không tìm thấy kết quả</div>
                        ) : (
                            filteredOptions.map((option) => (
                                <div
                                    key={option.value}
                                    onClick={() => handleSelect(option)}
                                    className={`flex items-center justify-between px-4 py-2 cursor-pointer hover:bg-gray-100
                                        ${value === option.value ? "bg-blue-50" : ""}`}
                                >
                                    <span>{option.label}</span>
                                    {value === option.value && <Check className="w-4 h-4 text-purple-700"/>}
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default InputSelection;

