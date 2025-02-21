/* eslint no-use-before-define: 0 */ // --> OFF
import {useForm} from "react-hook-form";

const SimpleForm = () => {

    const {register, handleSubmit} = useForm();
    const handleSubmitForm = (data) => {
        console.log(data);
    }

    const onSubmit = (data, e) => {
        e.preventDefault();
        alert(data)
        console.log(e)
        console.log(data)
    }

    return (
        <>
            <form onSubmit={handleSubmit(onSubmit)}>
                <input {...register("firstName")} placeholder="First name"/>
                <input {...register("lastName")} placeholder="Last name"/>
                <button type="submit">Submit</button>
            </form>
        </>
    );
}

export default SimpleForm;