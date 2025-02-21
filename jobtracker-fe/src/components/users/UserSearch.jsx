import {useState, useEffect, useRef} from 'react';
import Input from "../common/input/Input.jsx";
import Button from "../common/button/Button.jsx";

const UserSearch = ({handleSearch, searchCriteria, loading}) => {
    const searchTimeout = useRef(null);

    const [formData, setFormData] = useState({
        username: '',
        email: '',
        phoneNumber: '',
        firstname: '',
        sortBy: 'updateDate',
        sortDirection: 'asc'
    });

    useEffect(() => {
        setFormData({
            username: searchCriteria.username || '',
            email: searchCriteria.email || '',
            phoneNumber: searchCriteria.phoneNumber || '',
            firstname: searchCriteria.firstname || '',
            sortBy: searchCriteria.sortBy || 'updateDate',
            sortDirection: searchCriteria.sortDirection || 'asc'
        });
    }, [searchCriteria]);

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
            username: '',
            email: '',
            phoneNumber: '',
            firstname: '',
            sortBy: 'updateDate',
            sortDirection: 'asc'
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
                <form
                    onSubmit={handleSubmit}
                    className="p-4 sm:p-6 lg:p-8"
                >
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 lg:gap-6">
                        {/* Username Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                label="Username"
                                name="username"
                                placeholder="Nhập username"
                                value={formData.username}
                                onChange={handleInputChange}
                                disabled={loading}
                                className="w-full"
                                labelClassName="text-sm font-medium text-gray-700"
                                inputClassName="w-full mt-1"
                            />
                        </div>

                        {/* Email Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                label="Email"
                                name="email"
                                placeholder="Nhập email"
                                value={formData.email}
                                onChange={handleInputChange}
                                disabled={loading}
                                className="w-full"
                                labelClassName="text-sm font-medium text-gray-700"
                                inputClassName="w-full mt-1"
                            />
                        </div>

                        {/* Phone Number Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                name="phoneNumber"
                                label="Số điện thoại"
                                placeholder="Nhập số điện thoại"
                                value={formData.phoneNumber}
                                onChange={handleInputChange}
                                disabled={loading}
                                className="w-full"
                                labelClassName="text-sm font-medium text-gray-700"
                                inputClassName="w-full mt-1"
                            />
                        </div>

                        {/* First Name Field */}
                        <div className="w-full">
                            <Input
                                type="text"
                                name="firstname"
                                label="Tên"
                                placeholder="Nhập tên"
                                value={formData.firstname}
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

export default UserSearch;