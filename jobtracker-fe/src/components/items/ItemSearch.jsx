import {useState, useEffect, useRef, useMemo} from 'react';
import Input from "../common/input/Input.jsx";
import Button from "../common/button/Button.jsx";
import InputSelection from "../common/selection/InputSelection.jsx";
import useCategoryManagement from "../../hooks/category/useCategoryManagement.jsx";

const ItemSearch = ({handleSearch, keySearch, loading}) => {
    const searchTimeout = useRef(null);
    const [categories, setCategories] = useState([]);
    const [error, setError] = useState(null);
    const [formData, setFormData] = useState({
        itemName: '',
        itemCode: '',
        categoryCode: '',
        sortBy: 'updateDate',
        sortDirection: 'asc'
    });

    const {fetchAllCategories} = useCategoryManagement();

    // Fetch categories on component mount
    useEffect(() => {
        const loadCategories = async () => {
            try {
                const response = await fetchAllCategories();
                if (response && Array.isArray(response)) {
                    setCategories(response);
                }
            } catch (err) {
                setError(err.message || 'Failed to load categories');
                console.error('Error loading categories:', err);
            }
        };

        loadCategories();
    }, [fetchAllCategories]);

    // Transform categories for InputSelection
    const listCategories = useMemo(() => {
        return categories.map(category => ({
            value: category.categoryCode,
            label: `${category.categoryCode} - ${category.categoryName}`
        }));
    }, [categories]);

    // Sync with external keySearch changes
    useEffect(() => {
        setFormData({
            itemName: keySearch.itemName || '',
            itemCode: keySearch.itemCode || '',
            categoryCode: keySearch.categoryCode || '',
            sortBy: keySearch.sortBy || 'updateDate',
            sortDirection: keySearch.sortDirection || 'desc'
        });
    }, [keySearch]);

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setFormData(prev => ({...prev, [name]: value}));

        if (searchTimeout.current) {
            clearTimeout(searchTimeout.current);
        }
    };

    // Handle category selection
    const handleCategoryChange = (value) => {
        setFormData(prev => ({...prev, categoryCode: value}));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (searchTimeout.current) {
            clearTimeout(searchTimeout.current);
        }
        handleSearch(formData);
    };

    const handleReset = () => {
        if (searchTimeout.current) {
            clearTimeout(searchTimeout.current);
        }

        const resetData = {
            itemName: '',
            itemCode: '',
            categoryCode: '',
            sortBy: 'updateDate',
            sortDirection: 'desc'
        };

        setFormData(resetData);
        handleSearch(resetData);
    };

    useEffect(() => {
        return () => {
            if (searchTimeout.current) {
                clearTimeout(searchTimeout.current);
            }
        };
    }, []);

    return (
        <div className="w-full px-4 py-6 sm:px-6 lg:px-8">
            <div className="bg-white rounded-lg shadow-md border border-gray-200">
                <form onSubmit={handleSubmit} className="p-4 sm:p-6 lg:p-8">
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 lg:gap-6">
                        {/* Item Name Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                name="itemName"
                                label="Item Name"
                                placeholder="Nhập tên hạng mục"
                                value={formData.itemName}
                                onChange={handleInputChange}
                                disabled={loading}
                                className="w-full"
                                labelClassName="text-sm font-medium text-gray-700"
                                inputClassName="w-full mt-1"
                            />
                        </div>

                        {/* Item Code Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                name="itemCode"
                                label="Item Code"
                                placeholder="Nhập code hạng mục"
                                value={formData.itemCode}
                                onChange={handleInputChange}
                                disabled={loading}
                                className="w-full"
                                labelClassName="text-sm font-medium text-gray-700"
                                inputClassName="w-full mt-1"
                            />
                        </div>

                        {/* Category Selection Field */}
                        <div className="w-full">
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Category
                            </label>
                            <InputSelection
                                options={listCategories}
                                value={formData.categoryCode}
                                onChange={handleCategoryChange}
                                placeholder="Chọn mã danh mục"
                                disabled={loading}
                                className="w-full"
                            />
                        </div>
                    </div>

                    {/* Buttons Section */}
                    <div className="mt-6 flex flex-col sm:flex-row justify-center gap-3 sm:gap-4">
                        <Button
                            type="submit"
                            variant="primary"
                            tooltip="Tìm kiếm"
                            size="md"
                            className="w-full sm:w-32"
                            disabled={loading}
                            label="Search"
                            onClick={handleSubmit}
                        />

                        <Button
                            type="reset"
                            tooltip="Reset"
                            variant="outline"
                            size="md"
                            className="w-full sm:w-32"
                            onClick={handleReset}
                            label="Reset"
                            disabled={loading}
                        />
                    </div>
                </form>
            </div>
        </div>
    );
};

export default ItemSearch;