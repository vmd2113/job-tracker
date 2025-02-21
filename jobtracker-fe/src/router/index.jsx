// src/router/index.jsx
import React, {Suspense} from 'react';
import {createBrowserRouter, Navigate} from 'react-router-dom';
import LoadingSpinner from "../components/common/loading/LoadingSpinner.jsx";
import LoginPage from "../pages/auth/LoginPage.jsx";

import HomePage from "../pages/clients/HomePage.jsx";
import NotFound from "../pages/error/NotFound.jsx";

import AdminRoutes from "./routes/admin/adminRoute.jsx";
import ClientRoutes from "./routes/clients/clientRoute.jsx";
import RegisterPage from "../pages/auth/RegisterPage.jsx";
import Unauthorized from "../pages/error/Unauthorized.jsx";

// Loading component
const LoadingFallback = () => <><LoadingSpinner></LoadingSpinner></>

// Bọc các component lazy load trong Suspense
const wrapWithSuspense = (element) => (
    <Suspense fallback={<LoadingFallback/>}>
        {element}
    </Suspense>
);

// Xử lý routes để thêm Suspense
const processRoutes = (routes) => {
    return routes.map(route => ({
        ...route,
        element: wrapWithSuspense(route.element),
        children: route.children ? processRoutes(route.children) : undefined
    }));
};

const router = createBrowserRouter([
    {
        path: "/",
        children: [
            {
                index: true,
                element: <Navigate to="/login"/>
            },
            ...processRoutes(AdminRoutes),
            ...processRoutes(ClientRoutes)
        ]
    },
    {
        path: "home",
        element: <HomePage/>
    },
    {
        path: "login",
        element: <LoginPage/>
    },
    {
        path: "register",
        element: <RegisterPage/>
    },
    {
        path: "unauthorized",
        element: <Unauthorized/>
    },

    {
        path: "not-found",
        element: <NotFound/>
    }


]);

export default router;