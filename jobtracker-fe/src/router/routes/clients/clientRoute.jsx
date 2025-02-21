import ClientLayout from "../../../layout/clients/ClientLayout.jsx";
import HomePage from "../../../pages/clients/HomePage.jsx";
import PostListPage from "../../../pages/clients/PostListPage.jsx";
import PrivateRouter from "../PrivateRouter.jsx";
import React from "react";

const ClientRoutes = [
    {
        path: "clients/*",
        element: (
            <PrivateRouter requiredRoles={["ROLES_STAFF"]}>
                <ClientLayout/>
            </PrivateRouter>
        ),
        children: [
            {path: "home", element: <HomePage/>},
            {path: "posts", element: <PostListPage/>},

        ]
    },
    {}
];

export default ClientRoutes;
