import { useForm } from 'react-hook-form';

function HookForm() {
    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm();

    const onSubmit = (data) => {
        console.log('Form data:', data);
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div>
                <input
                    {...register("email", {
                        required: "Email is required",
                        pattern: {
                            value: /\S+@\S+\.\S+/,
                            message: "Invalid email format"
                        }
                    })}
                    placeholder="Email"
                />
                {errors.email && <span>{errors.email.message}</span>}
            </div>

            <div>
                <input
                    type="password"
                    {...register("password", {
                        required: "Password is required",
                        minLength: {
                            value: 6,
                            message: "Password must be at least 6 characters"
                        }
                    })}
                    placeholder="Password"
                />
                {errors.password && <span>{errors.password.message}</span>}
            </div>

            <button type="submit">Submit</button>
        </form>
    );
}