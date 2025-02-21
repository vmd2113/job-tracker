import React from 'react';
import error404 from "../../assets/images/404Error-amico.svg";

const NotFound = () => {
    return (
        <div className="flex items-center justify-center w-full min-h-screen p-4 bg-gray-50">
            <div className="text-center">
                <img
                    src={error404}
                    alt="Error 404"
                    className="mx-auto mb-8 max-w-full h-auto md:max-w-2xl lg:max-w-4xl"
                />
                <h1 className="text-2xl font-bold text-gray-800 md:text-3xl lg:text-4xl">
                    Page Not Found
                </h1>
                <p className="mt-4 text-gray-600 md:text-lg lg:text-xl">
                    The page you are looking for might have been removed or is temporarily unavailable.
                </p>
            </div>
        </div>
    );
};

export default NotFound;