// router/routes/admin.js
import {lazy} from "react";
import PrivateRouter from "../PrivateRouter.jsx";
import AdminLayout from "../../../layout/admin/AdminLayout.jsx";


const Dashboard = lazy(() => import("../../../pages/admin/Dashboard.jsx"));
const UserListPage = lazy(() => import("../../../pages/admin/Users/UserListPage.jsx"));
const DepartmentListPage = lazy(() => import("../../../pages/admin/departments/DepartmentListPage.jsx"));
const ItemListPage = lazy(() => import("../../../pages/admin/items/ItemListPage.jsx"));
const CategoryListPage = lazy(() => import("../../../pages/admin/categories/CategoryListPage.jsx"));

const WorkConfigListPage = lazy(() => import("../../../pages/admin/WorkConfigBusiness/WorkConfigListPage.jsx"));
const WorkListPage = lazy(() => import("../../../pages/admin/works/WorkListPage.jsx"));
const WorkTypeListPage = lazy(() => import("../../../pages/admin/WorkType/WorkTypeListPage.jsx"));
const UserProfilePage = lazy(() => import("../../../pages/admin/Users/UserProfilePage.jsx"));
const UserChangePasswordPage = lazy(() => import("../../../pages/admin/Users/UserChangePasswordPage.jsx"));
const AdminRoutes = [

    {
        path: "admin/*",
        element: (
            <PrivateRouter requiredRoles={["ROLES_ADMIN", "ROLES_MANAGER"]}>
                <AdminLayout/>
            </PrivateRouter>),
        children: [
            {
                path: "dashboard", element: <Dashboard/>, children: [
                    {path: "users", element: <UserListPage/>},
                    {path: "departments", element: <DepartmentListPage/>},
                    {path: "items", element: <ItemListPage/>},
                    {path: "categories", element: <CategoryListPage/>},

                    {path: "work-types", element: <WorkTypeListPage/>},
                    {path: "work-configs", element: <WorkConfigListPage/>},
                    {path: "works", element: <WorkListPage/>},
                    {path: "profile", element: <UserProfilePage/>},
                    {path: "change-password", element: <UserChangePasswordPage/>}
                ]
            },

        ],
    },
];

export default AdminRoutes;
