import React from 'react';
import logo from "/src/assets/images/logo-job-tracker-16-9.jpeg";

const Footer = () => {
    return (
        <footer className=" w-full bg-gray-100 rounded-lg ">
            <div className="w-full max-w-screen-xl mx-auto p-4 md:py-2 text-center">
                <div className="sm:flex-col sm:items-center sm:justify-center flex ">
                    <div className="justify-center items-center">


                        <ul className="flex flex-wrap items-center mb-6 text-sm font-medium text-gray-500 sm:mb-0 dark:text-gray-400">
                            <li>
                                <a href="#" className="hover:underline me-4 md:me-6">
                                    About
                                </a>
                            </li>
                            <li>
                                <a href="#" className="hover:underline me-4 md:me-6">
                                    Privacy Policy
                                </a>
                            </li>
                            <li>
                                <a href="#" className="hover:underline me-4 md:me-6">
                                    Licensing
                                </a>
                            </li>
                            <li>
                                <a href="#" className="hover:underline">
                                    Contact
                                </a>
                            </li>
                        </ul>

                    </div>
                    <div>
                            <span className="block text-sm text-gray-500 sm:text-center dark:text-gray-400">
                              © 2023{' '}
                                <a href="https://flowbite.com/" className="hover:underline">
                                    Duongw™</a>
                          . All Rights Reserved.
                            </span>
                    </div>
                </div>


            </div>
        </footer>
    );
};

export default Footer;
