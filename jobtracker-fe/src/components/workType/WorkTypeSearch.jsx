import React from 'react';
import Button from "../common/button/Button.jsx";
import Input from "../common/input/Input.jsx";

const WorkTypeSearch = ({
                           handleSearch,
                           keySearch,
                           loading
                       }) => {

    const searchTimeout = React.useRef(null);
    const [formData, setFormData] = React.useState({
        workTypeName: '',
        workTypeCode: '',
        sortBy: 'updateDate',
        sortDirection: 'asc'
    });

    React.useEffect(() => {
        setFormData({
            workTypeName: keySearch.workTypeName || '',
            workTypeCode: keySearch.workTypeCode || '',
            sortBy: keySearch.sortBy || 'updateDate',
            sortDirection: keySearch.sortDirection || 'asc'
        });
    }, [keySearch]);

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setFormData(prev => ({...prev, [name]: value}));

        if (searchTimeout.current) {
            clearTimeout(searchTimeout.current);
        }
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
            workTypeName: '',
            workTypeCode: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
        };

        setFormData(resetData);
        handleSearch(resetData);
    };

    React.useEffect(() => {
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
                        {/* Work Type Name Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                name="workTypeName"
                                label="Work Type Name"
                                placeholder="Nhập tên loại công việc"
                                value={formData.workTypeName}
                                onChange={handleInputChange}
                                disabled={loading}
                                className="w-full"
                                labelClassName="text-sm font-medium text-gray-700"
                                inputClassName="w-full mt-1"
                            />
                        </div>

                        {/* Work Type Code Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                name="workTypeCode"
                                label="Work Type Code"
                                placeholder="Nhập code loại công việc"
                                value={formData.workTypeCode}
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
                            type="submit"
                            variant="primary"
                            tooltip="Tìm kiếm"
                            size="md"
                            className="w-full sm:w-32"
                            label="Search"
                            disabled={loading}
                            onClick={handleSubmit}
                        />


                        <Button
                            type="reset"
                            tooltip="Reset"
                            variant="outline"
                            size="md"
                            className="w-full sm:w-32"
                            label="Reset"
                            onClick={handleReset}
                            disabled={loading}
                        />


                    </div>
                </form>
            </div>
        </div>
    );
};

export default WorkTypeSearch;