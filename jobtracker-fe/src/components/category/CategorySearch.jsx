import React, {useEffect, useRef, useState} from 'react';
import Input from "../common/input/Input.jsx";
import Button from "../common/button/Button.jsx";

const CategorySearch = ({handleSearch, keySearch, loading}) => {
    const searchTimeout = useRef(null);

    const [formSearchData, setFormSearchData] = useState({
        categoryName: '',
        categoryCode: '',
        sortBy: 'updateDate',
        sortDirection: 'asc'
    });

    useEffect(() => {
        setFormSearchData({
            categoryName: keySearch.categoryName || '',
            categoryCode: keySearch.categoryCode || '',
            sortBy: keySearch.sortBy || 'updateDate',
            sortDirection: keySearch.sortDirection || 'asc'
        });
    }, [keySearch]);

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setFormSearchData(prev => ({...prev, [name]: value}));

        if (searchTimeout.current) {
            clearTimeout(searchTimeout.current);
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (searchTimeout.current) {
            clearTimeout(searchTimeout.current);
        }
        handleSearch(formSearchData);
    };

    const handleReset = () => {
        if (searchTimeout.current) {
            clearTimeout(searchTimeout.current);
        }

        const resetData = {
            categoryName: '',
            categoryCode: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
        };

        setFormSearchData(resetData);
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
                <form
                    onSubmit={handleSubmit}
                    className="p-4 sm:p-6 lg:p-8"
                >
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 lg:gap-6">
                        {/* Category Name Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                label="Category Name"
                                name="categoryName"
                                placeholder="Nhập tên danh mục"
                                value={formSearchData.categoryName}
                                onChange={handleInputChange}
                                disabled={loading}
                                className="w-full"
                                labelClassName="text-sm font-medium text-gray-700"
                                inputClassName="w-full mt-1"
                            />
                        </div>

                        {/* Category Code Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                name="categoryCode"
                                label="Category Code"
                                placeholder="Nhập mã danh mục"
                                value={formSearchData.categoryCode}
                                onChange={handleInputChange}
                                disabled={loading}
                                className="w-full"
                                labelClassName="text-sm font-medium text-gray-700"
                                inputClassName="w-full mt-1"
                            />
                        </div>
                    </div>

                    {/* Buttons Section */}
                    <div className="mt-6 flex flex-col sm:flex-row justify-center gap-3 sm:gap-4">
                        <Button
                            type="search"
                            variant="primary"
                            tooltip="Tìm kiếm"
                            size="md"
                            className="w-full sm:w-32"
                            disabled={loading}
                            onClick={handleSubmit}
                        />
                        <Button
                            type="reset"
                            tooltip="Reset"
                            variant="outline"
                            size="md"
                            label="Reset"
                            className="w-full sm:w-32"
                            onClick={handleReset}
                            disabled={loading}
                        />
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CategorySearch;