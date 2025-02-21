import React, { useState } from 'react';

function BasicForm() {
    const [inputValue, setInputValue] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Submitted value:', inputValue);
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
            />
            <button type="submit">Submit</button>
        </form>
    );
}
export default BasicForm;
