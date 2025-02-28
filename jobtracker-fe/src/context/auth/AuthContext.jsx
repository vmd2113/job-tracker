import {createContext, useEffect, useState, useMemo, useContext} from "react";
import * as api from "../../api/authApi";
import axiosInstance from "../../api/axiosConfig.js";

const AuthContext = createContext(null);

export const AuthProvider = ({children}) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isInitialized, setIsInitialized] = useState(false);


    const login = async (username, password) => {
        try {
            const response = await api.login(username, password);
            console.log("LOGIN - RESPONSE AT AUTH", response);
            const userData = response.data
            console.log("LOGIN - USER DATA AT AUTH", userData);
            const accessToken = userData.data.accessToken;
            console.log("LOGIN - ACCESS TOKEN AT AUTH", accessToken);

            setUser(userData.data);
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("username", username);

            return {success: true, response: response.data};
        } catch (error) {
            return {
                success: false,
                error: error.message || "Login failed",
            };
        }
    };

    const logout = async () => {
        try {
            await api.logout();
            setUser(null);
            localStorage.removeItem("accessToken");
            localStorage.removeItem("username");
            return {success: true};
        } catch (error) {
            return {
                success: false,
                error: error.response?.data?.message || "Logout failed"
            };
        }finally {
            setUser(null);
            localStorage.removeItem("accessToken");
            axiosInstance.defaults.headers.common['Authorization'] = '';
        }
    };

    const register = async (userData) => {
        try {
            const response = await api.register(userData);
            console.log("REGISTER - RESPONSE AT AUTH", response);
            return {success: true, data: response.data};
        } catch (error) {
            return {
                success: false,
                error: error.response?.data?.message || "Registration failed",
            };
        }
    };

    const getCurrentUser = async () => {
        try {
            const token = localStorage.getItem("accessToken");
            if (token) {
                // add token to header
                axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
                const response = await api.getCurrentUser();
                console.log("GET CURRENT USER - RESPONSE AT AUTH", response);
                console.log("GET CURRENT USER - TOKEN AT AUTH", token);
                setUser(response.data.data); // set user data
                return response.data;
            } else {
                console.error("No token found");

            }


        } catch (error) {
            console.error("Failed to get current user:", error);
            // Clear token nếu có lỗi
            localStorage.removeItem("accessToken");
            axiosInstance.defaults.headers.common['Authorization'] = '';

            setUser(null);

            return null;
        } finally {
            setLoading(false);
            setIsInitialized(true);

        }
    }



    useEffect(() => {
        const checkAuthStatus = async () => {
            try {
                await getCurrentUser();

            } catch (error) {
                console.error("Auth status check failed:", error);
            }
        };

        checkAuthStatus();
    }, []);

    const value = useMemo(
        () => ({
            user,
            loading,
            isInitialized,
            login,
            logout,
            register,
        }),
        [user, loading]
    );

    return (
        <AuthContext.Provider value={value}>
            {!loading && isInitialized && children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};


