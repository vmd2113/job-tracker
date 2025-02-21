import { X, CheckCircle, AlertCircle, Info } from 'lucide-react';
import React, { useState, useEffect } from 'react';

const Toast = ({ message, type = 'success', duration = 3000, onClose }) => {
    const [progress, setProgress] = useState(100);
    const [isVisible, setIsVisible] = useState(true);

    useEffect(() => {
        const timer = setInterval(() => {
            setProgress((prev) => {
                if (prev > 0) {
                    return prev - (100 / (duration / 100));
                }
                return 0;
            });
        }, 100);

        const timeout = setTimeout(() => {
            setIsVisible(false);
            onClose?.();
        }, duration);

        return () => {
            clearInterval(timer);
            clearTimeout(timeout);
        };
    }, [duration, onClose]);

    const getIcon = () => {
        switch (type) {
            case 'success':
                return <CheckCircle className="w-5 h-5" />
            case 'error':
                return <AlertCircle className="w-5 h-5" />
            case 'info':
                return <Info className="w-5 h-5" />
            default:
                return <X className="w-5 h-5" />
        }
    }

    const getBgColor = () => {
        switch (type) {
            case 'success':
                return 'bg-green-50';
            case 'error':
                return 'bg-red-50';
            default:
                return 'bg-blue-50';
        }
    };

    const getProgressColor = () => {
        switch (type) {
            case 'success':
                return 'bg-green-500';
            case 'error':
                return 'bg-red-500';
            default:
                return 'bg-blue-500';
        }
    };

    if (!isVisible) return null;

    return (
        <div className={`z-9999 w-full min-w-[320px] max-w-xl ${getBgColor()} rounded-lg shadow-lg overflow-hidden`}>
            <div className="p-4">
                <div className="flex items-start space-x-3">
                    <div className="flex-shrink-0">
                        {getIcon()}
                    </div>
                    <div className="flex-1 break-words">
                        <p className="text-sm font-medium text-gray-900">{message}</p>
                    </div>
                    <div className="flex-shrink-0">
                        <button
                            className="rounded-md inline-flex text-gray-400 hover:text-gray-500 focus:outline-none"
                            onClick={() => {
                                setIsVisible(false);
                                onClose?.();
                            }}
                        >
                            <X className="h-5 w-5" />
                        </button>
                    </div>
                </div>
            </div>
            <div className="h-1 w-full bg-gray-200">
                <div
                    className={`h-full ${getProgressColor()} transition-all duration-100 ease-linear`}
                    style={{width: `${progress}%`}}
                />
            </div>
        </div>
    );
};

export default Toast;