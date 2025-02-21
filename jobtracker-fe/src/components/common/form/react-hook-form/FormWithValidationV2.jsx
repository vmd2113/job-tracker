import { useForm } from "react-hook-form";
import { notAdmin, notBlackListed, checkUserExists } from './validation';

function FormWithCustomValidation() {
    const { register, handleSubmit, formState: { errors } } = useForm();

    const onSubmit = data => {
        console.log(data);
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <input
                {...register("username", {
                    validate: {
                        notAdmin,
                        notBlackListed,
                        checkUserExists
                    }
                })}
            />
            {errors.username && <p className="error">{errors.username.message}</p>}
        </form>
    );
}

export default FormWithCustomValidation;
