import React from 'react';
import {useAuth} from "../../context/auth/AuthContext.jsx";
import {Navigate} from "react-router-dom";

const PostListPage = () => {

    const {user} = useAuth();
    console.log("POST LIST PAGE CLIENT");
    console.log("USER CLIENT", user);
    

    return (
        <div>
            <h5>welcome to post list page</h5>

        </div>
    );
};

export default PostListPage;