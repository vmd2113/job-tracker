````jsx

import React from "react";
import {ToastProvider, useToast} from "./components/common/toast/ToastContainer";
import MainLayout from "./pages/MainLayout.jsx";

function TestButtons() {
    const {showToast} = useToast();

    const handleSuccess = () => {
        showToast('Thêm mới thành công!', 'success', 3000);
    };

    const handleError = () => {
        showToast('Có lỗi xảy ra!', 'error', 5000);
    };

    return (
        <div>
            <button onClick={handleSuccess}>Test Success Toast</button>
            <button onClick={handleError}>Test Error Toast</button>
        </div>
    );
}

function App() {


    return (
        <ToastProvider>
          <MainLayout></MainLayout>

        </ToastProvider>
    );

}

export default App;
````