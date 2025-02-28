import React, {useState} from 'react';
import registerImage from "../../assets/images/register.svg";
import {useForm} from "react-hook-form";
import {useNavigate} from "react-router-dom";
import FormInput from "../../components/common/input/FormInput.jsx";
import Button from "../../components/common/button/Button.jsx";
import {useAuth} from "../../context/auth/AuthContext.jsx";

const RegisterPage = () => {
    const {control, handleSubmit} = useForm();

    const navigate = useNavigate();
    const {register} = useAuth();
    const [error, setError] = useState('');


    const onSubmit = async (data) => {
        console.log("Submit button clicked");
        const result = await register(data);
        console.log("RESULT REGISTER: ", result);

        if (result.success) {
            // Lấy roles từ response
            let roles = result.response?.data?.roles;
            console.log("ROLES IN LOGIN: ", roles);

            // Kiểm tra nếu roles là undefined, null hoặc không phải array
            if (!roles || !Array.isArray(roles)) {
                roles = []; // Gán một mảng rỗng nếu roles không hợp lệ
            }

            // Bây giờ an toàn để sử dụng .map()
            let listRoles = roles.map(role => role.roleCode);
            console.log("LIST ROLES IN LOGIN: ", listRoles);

            // Kiểm tra quyền và điều hướng
            if (listRoles.length > 0 && (listRoles.includes("ROLES_ADMIN") || listRoles.includes("ROLES_MANAGER"))) {
                return navigate("/admin/dashboard");
            }

            if (listRoles.length > 0 && listRoles.includes("ROLES_STAFF")) {
                return navigate("/clients/home");
            } else {
                //TODO unauthorized
                return navigate("/login");
            }
        } else {
            setError(result.error);
        }
    };


    return (
        <div className="container mx-auto p-4">
            <div
                className="flex space-y-4 text-center "
            >

                {/*form* container*/}
                <div className="flex flex-col space-y-4">

                    {/*title*/}
                    <div>

                    </div>


                    {/*form*/}
                    <div>
                        <form onSubmit={handleSubmit(onSubmit)}>

                            <div className="space-y-4">
                                <FormInput
                                    control={control}
                                    name="username"
                                    label="Username"
                                    clsName="w-72"
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
                                    labelClassName="text-sm text-left font-medium text-gray-700"
                                    inputClassName="w-full rounded-md"
                                ></FormInput>

                                <FormInput
                                    control={control}
                                    name="email"
                                    clsName="w-72"
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
                                    labelClassName="text-sm text-left font-medium text-gray-700"
                                    inputClassName="w-full rounded-md"
                                ></FormInput>


                                <FormInput
                                    control={control}
                                    name="phoneNumber"
                                    label="Số điện thoại"
                                    clsName="w-72"
                                    type="text"
                                    placeholder="Nhập số điện thoại"
                                    rules={{
                                        required: 'Số điện thoại là bắt buộc',
                                        pattern: {
                                            value: /^[0-9]/,
                                            message: 'Số điện thoại phải có 10 chữ số'
                                        },
                                        maxLength: {
                                            value: 11,
                                            message: 'Số điện thoại không được vượt quá 11 chữ số'
                                        }
                                    }}
                                    className="w-full"
                                    labelClassName="text-sm text-left font-medium text-gray-700"
                                    inputClassName="w-full rounded-md"
                                ></FormInput>

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
                                    labelClassName="text-sm text-left font-medium text-gray-700"
                                    inputClassName="w-full rounded-md"
                                />

                                <FormInput
                                    control={control}
                                    name="confimPassword"
                                    label="Confirm Password"
                                    type="password"
                                    placeholder="Nhập password"
                                    rules={{
                                        required: 'Confirm password là bắt buộc',
                                        minLength: {
                                            value: 8,
                                            message: 'Confirm Password phải có ít nhất 8 ký tự'
                                        }
                                    }}
                                    className="w-full"
                                    labelClassName="text-sm text-left font-medium text-gray-700"
                                    inputClassName="w-full rounded-md"
                                />

                            </div>

                        </form>

                        <Button
                            type="confirm"
                            className="w-full text-center mt-4"
                            label="Register"

                            onClick={handleSubmit(onSubmit)}
                        />

                    </div>


                </div>

                {/*image* container*/}
                <div className="flex flex-col items-center justify-center space-y-4">

                    {/*image*/}
                    <div></div>
                    {/*button to login page*/}
                    <div></div>
                </div>


            </div>
        </div>
    );

}
export default RegisterPage;


