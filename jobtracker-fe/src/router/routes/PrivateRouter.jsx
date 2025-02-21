// src/router/PrivateRouter.jsx
import {Navigate, useLocation} from 'react-router-dom';
import {useAuth} from "../../context/auth/AuthContext.jsx";
import LoadingSpinner from "../../components/common/loading/LoadingSpinner.jsx";
import {useEffect, useState} from "react";

const PrivateRouter = ({children, requiredRoles = []}) => {
    const {user, loading, isInitialized} = useAuth();
    const location = useLocation();
    const [isChecking, setIsChecking] = useState(true);
    const [hasAccess, setHasAccess] = useState(false);

    const checkRoleAccess = (userRoles, requiredRoles) => {
        if (!userRoles || !Array.isArray(userRoles)) return false;

        return requiredRoles.some(requiredRole => {
            return userRoles.some(userRole => {
                // Xử lý cả hai trường hợp
                if (typeof userRole === 'string') {
                    return userRole === requiredRole;
                }
                return userRole.roleCode === requiredRole;
            });
        });
    };

    useEffect(() => {
        const checkAccess = () => {
            if (loading || !isInitialized) {
                setIsChecking(true);
                return;
            }

            if (!user) {
                setIsChecking(false);
                setHasAccess(false);
                return;
            }

            // Kiểm tra roles
            if (requiredRoles.length > 0) {
                const userRoles = user.roles;
                console.log('User Roles:', userRoles);
                console.log('Required Roles:', requiredRoles);

                if (!userRoles || !Array.isArray(userRoles)) {
                    setHasAccess(false);
                } else {
                    // Kiểm tra xem user có role phù hợp không
                    const hasRequiredRole = requiredRoles.some(requiredRole =>
                        userRoles.some(userRole => userRole.roleCode === requiredRole)
                    );

                    console.log('Has Required Role:', hasRequiredRole);
                    setHasAccess(hasRequiredRole);
                }
            } else {
                setHasAccess(true);
            }

            setIsChecking(false);
        };

        checkAccess();
    }, [user, loading, isInitialized, requiredRoles]);


    if (loading || !isInitialized || isChecking) {
        return <LoadingSpinner/>;
    }

    if (!user) {
        return <Navigate to="/login" state={{from: location}} replace/>;
    }

    if (!hasAccess) {
        return <Navigate to="/unauthorized" replace/>;
    }

    return children;
};
export default PrivateRouter;