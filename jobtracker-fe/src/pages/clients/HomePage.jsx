import React from "react";
import Button from "../../components/common/button/Button.jsx";
import {useNavigate} from "react-router-dom";

function HomePage() {

    const navigate = useNavigate();
    const handleClick = () => {
        navigate("/clients/posts");
    }
    return (
        <div>
            <Button type = "other" onClick={handleClick}>next to post list</Button>
        </div>
    )
}

export default HomePage;
