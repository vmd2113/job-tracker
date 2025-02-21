import { useForm } from "react-hook-form";
function FormWithDependentValidation() {
    const {register, watch, formState: {errors}} = useForm();

    // Watch password để so sánh với confirmPassword
    const password = watch("password");

    return (
        <form>
            <input
                type="password"
                {...register("password", {
                    required: "Password là bắt buộc",
                    minLength: {
                        value: 8,
                        message: "Password phải có ít nhất 8 ký tự"
                    }
                })}
            />

            <input
                type="password"
                {...register("confirmPassword", {
                    validate: value =>
                        value === password || "Password không khớp"
                })}
            />
            {errors.confirmPassword && (
                <p>{errors.confirmPassword.message}</p>
            )}
        </form>
    );
}

export default FormWithDependentValidation;