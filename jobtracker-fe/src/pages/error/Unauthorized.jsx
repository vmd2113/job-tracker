import React from 'react';
import error401 from "../../assets/images/401ErrorUnauthorized-amico.svg";
import Button from "../../components/common/button/Button.jsx";
import {useLocation, useNavigate} from "react-router-dom";

const Unauthorized = () => {

    const navigate = useNavigate();
    const location = useLocation();

    return (
        <div className="min-h-screen w-full flex items-center justify-center p-4">
            <div className="w-full max-w-screen-lg flex flex-col items-center justify-center">
                <img
                    src={error401}
                    alt="Error 401"
                    className="mx-auto w-full md:w-1/3 h-auto select-none cursor-default"
                />

                <Button
                    type="back"
                    size="lg"
                    className="w-32 h-10 rounded-2xl mt-4 bg-purple-300 "
                    variant="primary"
                    label="Back"  // Override label mặc định
                    onClick={() => navigate('/login', {
                        state: {from: location.pathname}
                    })}
                />
            </div>
        </div>
    );
};

export default Unauthorized;