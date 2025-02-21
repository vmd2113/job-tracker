import React from "react";
import {RouterProvider} from "react-router-dom";
import router from "./router/index.jsx";
import {AuthProvider} from "./context/auth/AuthContext.jsx";
import {ToastProvider} from "./components/common/toast/ToastContainer.jsx";


function App() {


    return (
        <AuthProvider>
            <ToastProvider>

                <RouterProvider router={router}/>
            </ToastProvider>
        </AuthProvider>
    );
}

export default App;