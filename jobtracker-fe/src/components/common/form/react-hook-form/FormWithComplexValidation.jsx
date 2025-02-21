import { useForm } from "react-hook-form";

function FormWithComplexValidation() {
    const { register, formState: { errors } } = useForm();

    const validatePhoneNumber = (value) => {
        // Kiểm tra format số điện thoại Việt Nam
        if (!/^(0|\+84)[3|5|7|8|9][0-9]{8}$/.test(value)) {
            return "Số điện thoại không hợp lệ";
        }
        return true;
    };

    const validatePassword = (value) => {
        const errors = [];

        if (value.length < 8) {
            errors.push("Mật khẩu phải có ít nhất 8 ký tự");
        }
        if (!/[A-Z]/.test(value)) {
            errors.push("Mật khẩu phải có ít nhất 1 chữ hoa");
        }
        if (!/[a-z]/.test(value)) {
            errors.push("Mật khẩu phải có ít nhất 1 chữ thường");
        }
        if (!/[0-9]/.test(value)) {
            errors.push("Mật khẩu phải có ít nhất 1 số");
        }
        if (!/[!@#$%^&*]/.test(value)) {
            errors.push("Mật khẩu phải có ít nhất 1 ký tự đặc biệt");
        }

        return errors.length === 0 || errors.join(", ");
    };

    return (
        <form>
            <input
                {...register("phone", {
                    validate: validatePhoneNumber
                })}
                placeholder="Số điện thoại"
            />
            {errors.phone && <p>{errors.phone.message}</p>}

            <input
                type="password"
                {...register("password", {
                    validate: validatePassword
                })}
                placeholder="Mật khẩu"
            />
            {errors.password && <p>{errors.password.message}</p>}
        </form>
    );
}