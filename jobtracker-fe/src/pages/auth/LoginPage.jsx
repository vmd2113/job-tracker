import { useState } from 'react';
import { useForm } from "react-hook-form";
import PropTypes from "prop-types";
import { useNavigate } from "react-router-dom";
import FormInput from "../../components/common/input/FormInput.jsx";
import { useAuth } from "../../context/auth/AuthContext.jsx";

import loginImage from "../../assets/images/login.svg";
import Button from "../../components/common/button/Button.jsx";

const LoginPage = () => {
    const { control, handleSubmit } = useForm();
    const { login } = useAuth();
    const navigate = useNavigate();
    const [error, setError] = useState('');

    const navigateToRegister = () => {
        navigate("/register");
    }

    const onSubmit = async (data) => {
        const result = await login(data.username, data.password);

        if (result.success) {
            let roles = result.response?.data?.roles;
            let listRoles = roles.map(role => role.roleCode);

            if (listRoles !== null && (listRoles?.includes("ROLES_ADMIN") || listRoles?.includes("ROLES_MANAGER"))) {
                navigate("/admin/dashboard");
            } else if (listRoles !== null && listRoles?.includes("ROLES_STAFF")) {
                navigate("/clients/home");
            } else {
                navigate("/login");
            }
        } else {
            setError(result.error);
        }
    }

    return (
        <div className="container mx-auto py-8 px-4 md:py-16">
            <div className="max-w-6xl mx-auto bg-white rounded-xl shadow-lg overflow-hidden">
                <div className="flex flex-col md:flex-row">
                    {/* Form Section - Grows to take available space */}
                    <div className="w-full md:w-1/2 p-6 md:p-12">
                        <div className="max-w-md mx-auto">
                            <h2 className="text-2xl font-bold text-gray-800 mb-6">Welcome Back</h2>

                            {error && (
                                <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-600 rounded-lg">
                                    {error}
                                </div>
                            )}

                            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                                <FormInput
                                    control={control}
                                    name="username"
                                    rules={{
                                        required: "Username is required",
                                        minLength: {
                                            value: 6,
                                            message: "Username must be at least 6 characters"
                                        }
                                    }}
                                    label="Username"
                                    placeholder="Nhập username"
                                />

                                <FormInput
                                    control={control}
                                    name="password"
                                    rules={{
                                        required: "Password is required",
                                        minLength: {
                                            value: 6,
                                            message: "Password must be at least 6 characters"
                                        }
                                    }}
                                    label="Password"
                                    type="password"
                                    placeholder="Nhập password"
                                />

                                <div className="pt-4">
                                    <Button
                                        type="submit"
                                        variant="primary"
                                        label="Login"
                                        tooltip="đăng nhập"
                                        className="w-full rounded-xl"
                                    />
                                </div>

                                <div>
                                    <Button
                                        type="other"
                                        variant="secondary"
                                        label="Register"
                                        tooltip="đăng kí"
                                        className="w-full bg-purple-300 text-white rounded-xl"
                                        onClick={navigateToRegister}
                                    />
                                </div>
                            </form>
                        </div>
                    </div>

                    {/* Image Section */}
                    <div className="w-full md:w-1/2 bg-purple-50 flex items-center justify-center p-6 md:p-12">
                        <div className="select-none pointer-events-none">
                            <img
                                src={loginImage}
                                alt="Login illustration"
                                className="w-full max-w-md h-auto"
                                draggable="false"
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

LoginPage.propTypes = {
    location: PropTypes.shape({
        state: PropTypes.shape({
            from: PropTypes.string.isRequired
        })
    })
};

export default LoginPage;