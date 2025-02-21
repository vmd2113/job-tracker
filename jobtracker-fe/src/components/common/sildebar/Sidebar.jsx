import React, {useState, useEffect, useRef} from 'react';
import {
    Menu,
    ChevronRight,
    Building2,
    Users,
    Settings,
    Box,
    ListTodo,
    FileSpreadsheet,
    Clock,
    LayoutDashboard,
    LogOut,
    User,
    KeyRound
} from 'lucide-react';
import {useNavigate} from 'react-router-dom';
import Button from "../../common/button/Button.jsx";

const Sidebar = ({children}) => {
    const [isOpen, setIsOpen] = useState(true);
    const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
    const sidebarRef = useRef(null);
    const userMenuRef = useRef(null);
    const navigate = useNavigate();
    const LINK_ADMIN_DASHBOARD = '/admin/dashboard';

    const menuItems = {
        common: {
            title: 'Common',
            icon: Building2,
            items: [
                {title: 'User', icon: Users, link: `${LINK_ADMIN_DASHBOARD}/users`},
                {title: 'Phòng ban', icon: Building2, link: `${LINK_ADMIN_DASHBOARD}/departments`},
                {title: 'Danh mục', icon: Box, link: `${LINK_ADMIN_DASHBOARD}/categories`},
                {title: 'Hạng mục', icon: ListTodo, link: `${LINK_ADMIN_DASHBOARD}/items`},
            ]
        },
        wfm: {
            title: 'WFM',
            icon: FileSpreadsheet,
            items: [
                {title: 'Loại công việc', icon: ListTodo, link: `${LINK_ADMIN_DASHBOARD}/work-types`},
                {title: 'Cấu hình chuyển trạng thái', icon: Settings, link: `${LINK_ADMIN_DASHBOARD}/work-configs`},
                {title: 'Quản lý công việc', icon: Clock, link: `${LINK_ADMIN_DASHBOARD}/works`},
            ]
        }
    };

    const userMenuItems = [
        {title: 'Hồ sơ của tôi', icon: User, link: `${LINK_ADMIN_DASHBOARD}/profile`},
        {title: 'Đổi mật khẩu', icon: KeyRound, link: `${LINK_ADMIN_DASHBOARD}/change-password`},
        {title: 'Đăng xuất', icon: LogOut, link: `/logout`},
    ];

    // Handle responsive sidebar
    useEffect(() => {
        const handleResize = () => {
            if (window.innerWidth < 1024) {
                setIsOpen(false);
            } else {
                setIsOpen(true);
            }
        };

        window.addEventListener('resize', handleResize);
        handleResize();

        return () => window.removeEventListener('resize', handleResize);
    }, []);

    const handleMenuClick = (e, item) => {
        e.stopPropagation();
        if (item.link) {
            navigate(item.link);
            if (window.innerWidth < 1024) {
                setIsOpen(false);
            }
        }
    };

    // Close menus when clicking outside
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (sidebarRef.current && !sidebarRef.current.contains(event.target)) {
                if (window.innerWidth < 1024) {
                    setIsOpen(false);
                }
                setIsUserMenuOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    // Calculate user menu position
    const getUserMenuPosition = () => {
        if (!userMenuRef.current) return {};

        const rect = userMenuRef.current.getBoundingClientRect();
        const bottomSpace = window.innerHeight - rect.bottom;
        const isNearBottom = bottomSpace < 200; // threshold for bottom detection

        return {
            top: isNearBottom ? 'auto' : '0',
            bottom: isNearBottom ? '100%' : 'auto',
            marginBottom: isNearBottom ? '0.5rem' : '0',
            marginTop: isNearBottom ? '0' : '0.5rem'
        };
    };

    return (
        <div className="flex h-full relative">
            {/* Overlay for mobile */}
            {!isOpen && (
                <div className="lg:hidden">
                    <button
                        className="fixed top-4 left-4 z-50 p-2 bg-white rounded-lg shadow-lg"
                        onClick={() => setIsOpen(true)}
                    >
                        <Menu className="w-6 h-6 text-purple-600"/>
                    </button>
                </div>
            )}

            {isOpen && (
                <div
                    className="fixed inset-0 bg-black bg-opacity-50 z-20 lg:hidden"
                    onClick={() => setIsOpen(false)}
                />
            )}

            <div
                ref={sidebarRef}
                className={`
                    flex flex-col
                    fixed lg:relative
                    h-full
                    transition-all duration-300 ease-in-out
                    rounded-r-3xl
                    z-30
                    ${isOpen ? 'w-72 translate-x-0' : 'w-20 -translate-x-full lg:translate-x-0'}
                    bg-white
                    border-r
                    shadow-lg
                `}
            >
                {/* Dashboard Header */}
                <div
                    className={`
                        flex items-center p-4 cursor-pointer shrink-0
                        ${isOpen ? 'justify-between' : 'justify-center'}
                    `}
                >
                    <div className="flex items-center">
                        <LayoutDashboard className="w-6 h-6 text-purple-600 flex-shrink-0"/>
                        {isOpen && (
                            <span className="ml-2 text-xl text-purple-700 font-semibold truncate">
                                DASHBOARD
                            </span>
                        )}
                    </div>
                    {isOpen && (
                        <button
                            onClick={() => setIsOpen(false)}
                            className="lg:hidden"
                        >
                            <Menu className="w-6 h-6 text-gray-600"/>
                        </button>
                    )}
                </div>

                {/* Navigation */}
                <div className="flex-1 overflow-y-auto">
                    {Object.entries(menuItems).map(([key, category]) => (
                        <div key={key} className="mb-4">
                            <div className={`
                                flex items-center mb-2 px-4 py-2
                                ${isOpen ? 'justify-start' : 'justify-center'}
                            `}>
                                <category.icon className="w-5 h-5 text-gray-600 flex-shrink-0"/>
                                {isOpen && (
                                    <span className="ml-2 font-semibold text-gray-700 truncate">
                                        {category.title}
                                    </span>
                                )}
                            </div>
                            <ul className="space-y-1">
                                {category.items.map((item, index) => (
                                    <li key={index}>
                                        <Button
                                            icon={item.icon}
                                            label={isOpen ? item.title : ''}
                                            variant="outline"
                                            alignment="start"
                                            className={`
                                                w-full border-none transition-colors whitespace-nowrap overflow-hidden
                                                ${isOpen ? 'pl-8' : 'justify-center'} 
                                                text-gray-700 
                                                hover:bg-purple-100 hover:text-purple-700
                                                ${isOpen ? 'rounded-r-xl' : 'rounded-xl'}
                                            `}
                                            onClick={(e) => handleMenuClick(e, item)}
                                        />
                                    </li>
                                ))}
                            </ul>
                        </div>
                    ))}
                </div>

                {/* User Menu */}
                <div className="border-t mt-auto relative" ref={userMenuRef}>
                    <Button
                        icon={Users}
                        label={isOpen ? 'Trần Thùy Hiền' : ''}
                        variant="outline"
                        alignment="start"
                        className={`
                            w-full border-none transition-colors whitespace-nowrap overflow-hidden
                            ${isOpen ? 'pl-4' : 'justify-center'} 
                            text-gray-700 
                            hover:bg-purple-50 hover:text-purple-700
                            p-4
                        `}
                        onClick={() => setIsUserMenuOpen(!isUserMenuOpen)}
                    >
                        {isOpen && (
                            <>
                                <span className="text-sm text-gray-500 ml-auto">Admin</span>
                                <ChevronRight
                                    className={`w-4 h-4 ml-2 transform transition-transform ${isUserMenuOpen ? 'rotate-90' : ''}`}/>
                            </>
                        )}
                    </Button>

                    {/* UserInfo Dropdown với z-index cao hơn */}
                    {isUserMenuOpen && (
                        <div
                            className={`
                                absolute 
                                ${isOpen ? 'left-full' : 'right-0 lg:left-full'} 
                                w-48 bg-white border rounded-lg shadow-lg
                                z-40
                            `}
                            style={getUserMenuPosition()}
                        >
                            {userMenuItems.map((item, index) => (
                                <Button
                                    key={index}
                                    icon={item.icon}
                                    label={item.title}
                                    variant="outline"
                                    alignment="start"
                                    className="w-full border-none transition-colors text-gray-700 hover:bg-purple-50 hover:text-purple-700 p-3"
                                    onClick={(e) => {
                                        handleMenuClick(e, item);
                                        setIsUserMenuOpen(false);
                                    }}
                                />
                            ))}
                        </div>
                    )}
                </div>
            </div>


            {/*<main className="flex-1 p-4 overflow-auto relative lg:ml-20">*/}
            {/*    {children}*/}
            {/*</main>*/}
        </div>
    );
};

export default Sidebar;