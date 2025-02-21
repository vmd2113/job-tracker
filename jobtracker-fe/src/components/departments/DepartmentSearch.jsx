import React from 'react';
import Button from "../common/button/Button.jsx";
import Input from "../common/input/Input.jsx";

const DepartmentSearch = ({
                             handleSearch,
                             keySearch,
                             loading
                         }) => {

    const searchTimeout = React.useRef(null);

    const[formSearchData, setFormSearchData] = React.useState({
        departmentName: '',
        departmentCode: '',
        sortBy: 'updateDate',
        sortDirection: 'asc'
    });

    React.useEffect(() => {
        setFormSearchData({
            departmentName: keySearch.departmentName || '',
            departmentCode: keySearch.departmentCode || '',
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
            departmentName: '',
            departmentCode: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
        };

        setFormSearchData(resetData);
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
                        {/* Department Name Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                name="departmentName"
                                label="Department Name"
                                placeholder="Nhập tên phòng ban"
                                value={formSearchData.departmentName}
                                onChange={handleInputChange}
                                disabled={loading}
                                className="w-full"
                                labelClassName="text-sm font-medium text-gray-700"
                                inputClassName="w-full mt-1"
                            />
                        </div>

                        {/* Department Code Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                name="departmentCode"
                                label="Department Code"
                                placeholder="Nhập code phòng ban"
                                value={formSearchData.departmentCode}
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
                            disabled={loading}
                            label="Search"
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

export default DepartmentSearch;