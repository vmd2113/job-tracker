import React, { useState } from 'react';
import registerImage from "../../assets/images/register.svg";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import FormInput from "../../components/common/input/FormInput.jsx";
import Button from "../../components/common/button/Button.jsx";
import { useAuth } from "../../context/auth/AuthContext.jsx";

const RegisterPage = () => {
    const { control, handleSubmit, watch } = useForm();
    const navigate = useNavigate();
    const { register } = useAuth();
    const [error, setError] = useState('');

    // Watch the password field for validation
    const password = watch("password", "");

    const navigateToLogin = () => {
        navigate("/login");
    };

    const onSubmit = async (data) => {
        // Check if passwords match before submitting
        if (data.password !== data.confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        const result = await register(data);

        if (result.success) {
            let roles = result.response?.data?.roles || [];
            let listRoles = roles.map(role => role.roleCode);

            if (listRoles.length > 0 && (listRoles.includes("ROLES_ADMIN") || listRoles.includes("ROLES_MANAGER"))) {
                navigate("/admin/dashboard");
            } else if (listRoles.length > 0 && listRoles.includes("ROLES_STAFF")) {
                navigate("/clients/home");
            } else {
                navigate("/login");
            }
        } else {
            setError(result.error);
        }
    };

    return (
        <div className="container mx-auto py-8 px-4 md:py-12">
            <div className="max-w-6xl mx-auto bg-white rounded-xl shadow-2xl overflow-hidden">
                <div className="flex flex-col md:flex-row">
                    {/* Form Section */}
                    <div className="w-full md:w-1/2 p-6 md:p-10">
                        <div className="max-w-md mx-auto">
                            <h2 className="text-2xl font-bold text-gray-800 mb-6">Create Account</h2>

                            {error && (
                                <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-600 rounded-lg">
                                    {error}
                                </div>
                            )}

                            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                                <FormInput
                                    control={control}
                                    name="username"
                                    label="Username"
                                    type="text"
                                    placeholder="Nhập tên tài khoản"
                                    rules={{
                                        required: 'Username là bắt buộc',
                                        minLength: {
                                            value: 6,
                                            message: 'Username phải có ít nhất 6 ký tự'
                                        },
                                        maxLength: {
                                            value: 500,
                                            message: 'Username không được vượt quá 500 ký tự'
                                        },
                                        pattern: {
                                            value: /^[a-zA-Z0-9_]+$/,
                                            message: 'Username chỉ được chứa chữ cái, số và dấu gạch dưới'
                                        }
                                    }}
                                    className="w-full"
                                    labelClassName="text-sm font-medium text-gray-700"
                                    inputClassName="w-full rounded-md"
                                />

                                <FormInput
                                    control={control}
                                    name="email"
                                    label="Email"
                                    type="email"
                                    placeholder="Nhập email"
                                    rules={{
                                        required: 'Email là bắt buộc',
                                        pattern: {
                                            value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                            message: 'Email không hợp lệ'
                                        },
                                        maxLength: {
                                            value: 500,
                                            message: 'Email không được vượt quá 500 ký tự'
                                        }
                                    }}
                                    className="w-full"
                                    labelClassName="text-sm font-medium text-gray-700"
                                    inputClassName="w-full rounded-md"
                                />

                                <FormInput
                                    control={control}
                                    name="phoneNumber"
                                    label="Số điện thoại"
                                    type="text"
                                    placeholder="Nhập số điện thoại"
                                    rules={{
                                        required: 'Số điện thoại là bắt buộc',
                                        pattern: {
                                            value: /^[0-9]{10,11}$/,
                                            message: 'Số điện thoại phải có 10-11 chữ số'
                                        }
                                    }}
                                    className="w-full"
                                    labelClassName="text-sm font-medium text-gray-700"
                                    inputClassName="w-full rounded-md"
                                />

                                <FormInput
                                    control={control}
                                    name="password"
                                    label="Password"
                                    type="password"
                                    placeholder="Nhập password"
                                    rules={{
                                        required: 'Password là bắt buộc',
                                        minLength: {
                                            value: 8,
                                            message: 'Password phải có ít nhất 8 ký tự'
                                        }
                                    }}
                                    className="w-full"
                                    labelClassName="text-sm font-medium text-gray-700"
                                    inputClassName="w-full rounded-md"
                                />

                                <FormInput
                                    control={control}
                                    name="confirmPassword"
                                    label="Confirm Password"
                                    type="password"
                                    placeholder="Nhập lại password"
                                    rules={{
                                        required: 'Confirm password là bắt buộc',
                                        minLength: {
                                            value: 8,
                                            message: 'Confirm password phải có ít nhất 8 ký tự'
                                        },
                                        validate: value =>
                                            value === password || "Passwords do not match"
                                    }}
                                    className="w-full"
                                    labelClassName="text-sm font-medium text-gray-700"
                                    inputClassName="w-full rounded-md"
                                />

                                <div className="pt-4 space-y-3">
                                    <Button
                                        type="confirm"
                                        className="w-full rounded-xl"
                                        variant="primary"
                                        label="Register"
                                    />


                                    <Button
                                        type="other"
                                        label="Already have an account? Login"
                                        variant="secondary"
                                        className="w-full bg-purple-300 text-white rounded-xl"
                                        onlabel="Already have an account? Login"
                                        onClick={navigateToLogin}
                                    />
                                </div>
                            </form>
                        </div>
                    </div>

                    {/* Image Section */}
                    <div className="w-full md:w-1/2 bg-purple-50 flex items-center justify-center p-6 md:p-10 order-first md:order-last">
                        <div className="select-none pointer-events-none">
                            <img
                                src={registerImage}
                                alt="Register illustration"
                                className="w-full max-w-md h-auto"
                                draggable="false"
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default RegisterPage;