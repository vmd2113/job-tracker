import { useForm } from "react-hook-form";

function FormWithContextValidation() {
    const { register, getValues, formState: { errors } } = useForm();

    return (
        <form>
            <input {...register("startDate")} type="date" />
            <input
                type="date"
                {...register("endDate", {
                    validate: {
                        futureDate: (value) => {
                            // Kiểm tra endDate phải sau startDate
                            const startDate = getValues("startDate");
                            return new Date(value) > new Date(startDate) ||
                                "Ngày kết thúc phải sau ngày bắt đầu";
                        }
                    }
                })}
            />
            {errors.endDate && <p>{errors.endDate.message}</p>}
        </form>
    );
}