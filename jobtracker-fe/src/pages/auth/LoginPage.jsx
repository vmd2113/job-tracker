import React, {useState} from 'react';
import {useForm} from "react-hook-form";
import PropTypes from "prop-types";
import {Navigate, useNavigate} from "react-router-dom";
import FormInput from "../../components/common/input/FormInput.jsx";
import {useAuth} from "../../context/auth/AuthContext.jsx";


const LoginPage = () => {

    const {control, handleSubmit} = useForm()
    const {login} = useAuth();

    const navigate = useNavigate();
    const [error, setError] = useState('');

    const onSubmit = async (data) => {
        const result = await login(data.username, data.password);
        console.log("RESULT LOGIN: ", result)

        if (result.success) {

            let roles = result.response?.data?.roles; // list object roles{roleId, roleCode, roleName}
            console.log("ROLES IN LOGIN: ", roles)

            let listRoles = roles.map(role => role.roleCode);
            console.log("LIST ROLES IN LOGIN: ", listRoles)



            if (listRoles !== null && (listRoles?.includes("ROLES_ADMIN") || listRoles?.includes("ROLES_MANAGER"))) {
                return navigate("/admin/dashboard")
            }

            if (listRoles !== null && listRoles?.includes("ROLES_STAFF")) {
                return navigate("/clients/home")
            }
            else {
                //TODO unauthorized
                return navigate("/login")
            }


        } else {
            setError(result.error);
        }
    }
    return (
        <div className="">
            <form onSubmit={handleSubmit(onSubmit)}>
                {error && <div className="error-message">{error}</div>}
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
                <button type="submit" className="submit-button">
                    Login
                </button>


            </form>
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