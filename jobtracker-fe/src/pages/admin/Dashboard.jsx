// pages/admin/Dashboard.jsx
import React from "react";
import {useAuth} from "../../context/auth/AuthContext.jsx";
import {Navigate, Outlet} from "react-router-dom";
import {ToastProvider} from "../../components/common/toast/ToastContainer.jsx";

const Dashboard = () => {
    const {user} = useAuth();

    console.log("USER AT DASHBOARD", user);
    return (
        <div className="flex flex-col w-full">
            <ToastProvider>
                <main className="flex-1 p-2">
                    <Outlet/>
                </main>
            </ToastProvider>
        </div>
    );
};

export default Dashboard;