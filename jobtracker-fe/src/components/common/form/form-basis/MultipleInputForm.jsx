import React, { useState } from 'react';

function MultipleInputsForm() {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Form data:', formData);
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                name="username"
                value={formData.username}
                onChange={handleChange}
                placeholder="Username"
            />
            <input
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="Email"
            />
            <input
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Password"
            />
            <button type="submit">Submit</button>
        </form>
    );
}

export default MultipleInputsForm;