import React from 'react';
import {useController} from 'react-hook-form';
import Input from './Input';

const FormInput = ({
                       control,
                       name,
                       clsName,
                       rules,
                       label,
                       ...inputProps
                   }) => {
    const {
        field: {onChange, onBlur, value, ref},
        fieldState: {error, isTouched}
    } = useController({
        name,
        control,
        rules
    });

    return (
        <Input
            {...inputProps}
            ref={ref}
            name={name}
            className={clsName}
            value={value || ''}
            onChange={onChange}
            onBlur={onBlur}
            label={label}
            error={error?.message}
            required={rules?.required !== undefined}
        />
    );
};

export default FormInput;