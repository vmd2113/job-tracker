// src/layout/admin/AdminLayout.jsx
import React from "react";
import { Outlet, Link, useNavigate, Navigate } from "react-router-dom";
import { useAuth } from "../../context/auth/AuthContext.jsx";
import Header from "../../components/common/header/Header.jsx";
import Sidebar from "../../components/common/sildebar/Sidebar.jsx";

function AdminLayout() {
    const { user } = useAuth();
    console.log("USER AT ADMIN LAYOUT", user);
    const role = user?.data?.roles?.includes("ROLES_ADMIN") ? "admin" : "client";
    console.log("ROLE AT ADMIN LAYOUT", role);

    return (
        <div className="flex h-screen w-full bg-gray-100">
            <div className="flex flex-col w-full">

                <div className="flex flex-1 overflow-hidden">
                    <Sidebar />
                    <main className="flex-1 w-full overflow-x-hidden overflow-y-auto">
                        <Outlet />
                    </main>
                </div>
            </div>
        </div>
    );
}

export default AdminLayout;