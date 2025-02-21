import React from 'react';
import PropTypes from 'prop-types';

const CheckboxItem = ({id, checked, indeterminate, onChange, label}) => {
    const ref = React.useRef();

    React.useEffect(() => {
        if (ref.current) {
            ref.current.indeterminate = indeterminate || false;
        }
    }, [indeterminate]);

    return (
        <div className="flex items-center">
            <input
                ref={ref}
                type="checkbox"
                checked={checked}
                onChange={(e) => onChange(id, e.target.checked)}
                className="mr-2"
            />
            {label && <span>{label}</span>}
        </div>
    );
};

CheckboxItem.defaultProps = {
    checked: false,
    indeterminate: false,
    label: null,
};

CheckboxItem.propTypes = {
    id: PropTypes.string.isRequired,
    checked: PropTypes.bool,
    indeterminate: PropTypes.bool,
    onChange: PropTypes.func,
    label: PropTypes.string,
};

export default CheckboxItem;
