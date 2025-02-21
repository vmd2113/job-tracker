import Header from "../../components/common/header/Header.jsx";
import Sidebar from "../../components/common/sildebar/Sidebar.jsx";
import {Outlet} from "react-router-dom";



const MainLayout = () => {
    return (
        <div className="min-h-screen flex flex-col">
           <Header/>
            <div className="flex flex-1 bg-gray-100">
                <Sidebar/>
                <Outlet/>
            </div>
        </div>
    );
}

export default MainLayout;