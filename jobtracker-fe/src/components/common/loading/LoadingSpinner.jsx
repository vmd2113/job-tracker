import React from 'react';

const LoadingSpinner = () => {
    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg p-8 relative">
                <div className="w-12 h-12 border-4 border-gray-200 border-t-purple-500 rounded-full animate-spin"></div>
            </div>
        </div>
    );
};

export default LoadingSpinner;