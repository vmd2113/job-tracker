import React, {useState, useRef, useEffect} from "react";
import Button from "../../common/button/Button.jsx";
import logo from "/src/assets/images/logo-job-tracker-16-9.jpeg";

const Header = ({children}) => {
    const [nav, setNav] = useState(false);
    const [profileDropdown, setProfileDropdown] = useState(false);

    // Tham chiếu đến phần tử chứa dropdown để kiểm tra click bên ngoài
    const profileRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (profileRef.current && !profileRef.current.contains(event.target)) {
                setProfileDropdown(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    return (
        <header className="w-full shadow">
            <div className="max-w-screen-xl mx-auto">
                <div className="grid grid-cols-6">
                    {/* Logo section */}
                    <div className="col-span-1 pl-2 py-4">
                        <a href="#" className="flex items-center">
                            <img
                                src={logo}
                                className="h-16 w-48 rounded-4xl object-contain"
                                alt="Logo"
                            />
                        </a>
                    </div>

                    {/* Navigation section */}
                    <nav className="col-span-5 pr-6">
                        <div className="flex justify-end items-center h-full">
                            <div
                                className={`flex-col md:flex md:flex-row items-center md:w-auto transition-all duration-300 ${
                                    nav
                                        ? "absolute top-20 left-0 w-full bg-white shadow-md p-4 md:relative md:top-0 md:w-auto md:bg-transparent md:shadow-none"
                                        : "hidden md:flex gap-6"
                                }`}
                            >
                                <ul className="flex flex-col md:flex-row md:gap-8 gap-2">
                                    <li>
                                        <div className="w-24">
                                            <a
                                                href="#"
                                                className="py-4 h-8 rounded-2xl w-24 flex justify-center items-center text-purple-900 hover:text-white hover:bg-purple-700 transition-all duration-300 ease-in-out"
                                                aria-current="page"
                                            >
                                                <span>Trang chủ</span>
                                            </a>
                                        </div>
                                    </li>
                                    <li>
                                        <a
                                            href="#"
                                            className="py-4 h-8 rounded-2xl w-24 flex justify-center items-center text-purple-900 hover:text-white hover:bg-purple-700 transition-all duration-300 ease-in-out"
                                            aria-current="page"
                                        >
                                            <span>Quản lý</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a
                                            href="#"
                                            className="py-4 h-8 rounded-2xl w-24 flex justify-center items-center text-purple-900 hover:text-white hover:bg-purple-700 transition-all duration-300 ease-in-out"
                                            aria-current="page"
                                        >
                                            <span>Thông báo</span>
                                        </a>
                                    </li>
                                    {/* Dropdown cho Profile */}
                                    <li className="relative" ref={profileRef}>
                                        <button
                                            onClick={() => setProfileDropdown((prev) => !prev)}
                                            className="py-4 h-8 rounded-2xl w-24 flex justify-center items-center text-purple-900 hover:text-white hover:bg-purple-700 transition-all duration-300 ease-in-out focus:outline-none"
                                        >
                                            <span>Profile</span>
                                        </button>
                                        {profileDropdown && (
                                            <div
                                                className="absolute right-0 mt-2 w-40 bg-white border border-gray-200 rounded-md shadow-lg z-10">
                                                <a
                                                    href="#"
                                                    className="block px-4 py-2 text-gray-800 hover:bg-gray-100"
                                                >
                                                    User
                                                </a>
                                                <a
                                                    href="#"
                                                    className="block px-4 py-2 text-gray-800 hover:bg-gray-100"
                                                >
                                                    Đăng xuất
                                                </a>
                                            </div>
                                        )}
                                    </li>
                                </ul>
                            </div>

                            {/* Nút menu dành cho mobile */}
                            <div className="md:hidden flex items-center">
                                <button
                                    type="button"
                                    className="inline-flex items-center p-2 ml-1 text-white rounded-lg hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-gray-200"
                                    aria-controls="mobile-menu"
                                    aria-expanded={nav}
                                    onClick={() => setNav(!nav)}
                                >
                                    <span className="sr-only">Open main menu</span>
                                    {nav ? (
                                        <svg
                                            className="w-6 h-6"
                                            fill="currentColor"
                                            viewBox="0 0 20 20"
                                            xmlns="http://www.w3.org/2000/svg"
                                        >
                                            <path
                                                fillRule="evenodd"
                                                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                                clipRule="evenodd"
                                            ></path>
                                        </svg>
                                    ) : (
                                        <svg
                                            className="w-6 h-6"
                                            fill="currentColor"
                                            viewBox="0 0 20 20"
                                            xmlns="http://www.w3.org/2000/svg"
                                        >
                                            <path
                                                fillRule="evenodd"
                                                d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
                                                clipRule="evenodd"
                                            ></path>
                                        </svg>
                                    )}
                                </button>
                            </div>
                        </div>
                    </nav>
                </div>
            </div>
        </header>
    );
};

export default Header;
