import React, { useState } from 'react';

function ValidationForm() {
    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });
    const [errors, setErrors] = useState({});

    const validateForm = () => {
        let newErrors = {};

        if (!formData.email) {
            newErrors.email = 'Email is required';
        } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
            newErrors.email = 'Email is invalid';
        }

        if (!formData.password) {
            newErrors.password = 'Password is required';
        } else if (formData.password.length < 6) {
            newErrors.password = 'Password must be at least 6 characters';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (validateForm()) {
            console.log('Form is valid:', formData);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    return (
        <form onSubmit={handleSubmit} noValidate>
            <div>
                <input
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="Email"
                />
                {errors.email && <span style={{color: 'red'}}>{errors.email}</span>}
            </div>
            <div>
                <input
                    name="password"
                    type="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="Password"
                />
                {errors.password && <span style={{color: 'red'}}>{errors.password}</span>}
            </div>
            <button type="submit">Submit</button>
        </form>
    );
}

export default ValidationForm