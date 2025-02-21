// src/layout/clients/ClientLayout.jsx
import React from "react";
import { Outlet, Link } from "react-router-dom";
import {useAuth} from "../../context/auth/AuthContext.jsx";



function ClientLayout() {
    const { user, logout } = useAuth();

    return (
        <div className="flex flex-col min-h-screen bg-gray-100">
            <nav className="bg-blue-600 text-white py-4 px-6 shadow-md flex justify-between items-center">
                <h2 className="text-xl font-bold">Client Portal</h2>
                <div className="flex gap-4">
                    <Link to="/clients/home" className="hover:text-gray-200">Home</Link>
                    {!user ? (
                        <Link to="/login" className="hover:text-gray-200">Login</Link>
                    ) : (
                        <button
                            onClick={logout}
                            className="bg-red-500 px-4 py-2 rounded hover:bg-red-600 transition"
                        >
                            Logout
                        </button>
                    )}
                </div>
            </nav>

            <main className="flex-grow p-6">
                <Outlet />
            </main>

            <footer className="bg-gray-800 text-white text-center py-4">
                <p>© 2024 Client Portal. All rights reserved.</p>
            </footer>
        </div>
    );
}

export default ClientLayout;
