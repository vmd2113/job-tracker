// ToastContainer.jsx
import React, {useState, createContext, useContext, useCallback} from "react";
import Toast from "./Toast";

export const ToastContext = createContext();

export const useToast = () => {
    const context = useContext(ToastContext);
    if (!context) {
        throw new Error('useToast must be used within a ToastProvider');
    }
    return context;
};

export const ToastProvider = ({children}) => {
    const [toasts, setToasts] = useState([]);
    const MAX_TOASTS = 3;

    const showToast = useCallback((message, type = 'success', duration = 3000) => {
        const id = Date.now();
        setToasts(prev => {
            // Nếu đã có MAX_TOASTS toast, loại bỏ toast cũ nhất
            const updatedToasts = prev.length >= MAX_TOASTS
                ? prev.slice(1)
                : prev;
            return [...updatedToasts, {id, message, type, duration}];
        });
    }, []);

    const removeToast = useCallback((id) => {
        setToasts(prev => prev.filter(toast => toast.id !== id));
    }, []);

    return (
        <ToastContext.Provider value={{showToast}}>
            {children}
            <div className="fixed inset-0 pointer-events-none flex flex-col items-end justify-end p-4 gap-4 z-[9999]">
                <div className="flex flex-col-reverse gap-4">
                    {toasts.map((toast, index) => (
                        <div
                            key={toast.id}
                            className="pointer-events-auto transform transition-all duration-300 ease-in-out"
                            style={{
                                opacity: 1,
                                transform: `translateY(0)`,
                            }}
                        >
                            <Toast
                                message={toast.message}
                                type={toast.type}
                                duration={toast.duration}
                                onClose={() => removeToast(toast.id)}
                            />
                        </div>
                    ))}
                </div>
            </div>
        </ToastContext.Provider>
    );
};