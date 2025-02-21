import React from 'react';
import error404 from "../../assets/images/404Error-amico.svg";
const NotFound = () => {
    return (
        <div className="min-h-screen w-full flex items-center justify-center p-4">
            <div className="w-full max-w-screen-lg">
                <img
                    src={error404}
                    alt="Error 404"
                    className="mx-auto w-full md:w-1/3 h-auto select-none cursor-default"
                />
            </div>
        </div>
    );
};

export default NotFound;