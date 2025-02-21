function FormWithSimpleValidation() {
  const { register, handleSubmit, formState: { errors } } = useForm();

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input
        {...register("email", {
          required: "Email là bắt buộc",
          pattern: {
            value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
            message: "Email không hợp lệ"
          }
        })}
      />
      {errors.email && <p>{errors.email.message}</p>}

      <input
        {...register("age", {
          min: { value: 18, message: "Tuổi phải lớn hơn 18" },
          max: { value: 99, message: "Tuổi phải nhỏ hơn 99" }
        })}
        type="number"
      />
      {errors.age && <p>{errors.age.message}</p>}
    </form>
  );
}

export default FormWithSimpleValidation;