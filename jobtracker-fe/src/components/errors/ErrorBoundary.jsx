import React from 'react';


function ErrorBoundary({ children }) {
    return (
        <div className="error-boundary">
            <div className="error-content">
                {children}
            </div>
        </div>
    );
}

export default ErrorBoundary;