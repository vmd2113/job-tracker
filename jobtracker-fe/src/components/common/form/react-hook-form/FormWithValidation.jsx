import { useForm } from "react-hook-form";

function FormWithCustomValidation() {
    const { register, handleSubmit, formState: { errors } } = useForm();

    return (
        <form onSubmit={handleSubmit(data => console.log(data))}>
            {/* Validation với hàm tùy chỉnh */}
            <input
                {...register("username", {
                    validate: {
                        notAdmin: (value) => {
                            return value !== "admin" || "Không được sử dụng username là admin";
                        },
                        notBlackListed: (value) => {
                            return !["test", "fake", "dummy"].includes(value) ||
                                "Username này không được phép sử dụng";
                        },
                        // Validation bất đồng bộ
                        checkUserExists: async (value) => {
                            const response = await fetch(`/api/check-username/${value}`);
                            return (await response.json()).isAvailable || "Username đã tồn tại";
                        }
                    }
                })}
            />
            {errors.username && <p className="error">{errors.username.message}</p>}
        </form>
    );
}
export default FormWithCustomValidation;