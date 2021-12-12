import React, { useState } from 'react';
import axios from "axios";
import { Helmet } from "react-helmet";

export function LoginPage(props) {


    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");


    function handleSubmit(event) {
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/user/login`,
            'data': {
                username: username,
                password: password
            },
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true
        }).then((response) => {
            //dispatch(load(response.data));
            props.history.push("/home");
        }).catch(function (error) {
            if (error.response) {
                alert(error.response.data);
            }
        });
        event.preventDefault();
    }

    function handleRedirectToRegister() {
        props.history.push("/register")
    }


    return (<div className="loginpage">
        <Helmet>
            <meta charSet="UTF-8" />
            <title>Login</title>
        </Helmet>
        <div className="login">
            <form className="loginform" onSubmit={handleSubmit} method="POST">
                <label><b>Username
                </b>
                </label><br />
                <input name="username" type="text" onChange={e => setUsername(e.target.value)}></input><br />
                <label><b>Password
                </b>
                </label><br />
                <input name="password" type="password" onChange={e => setPassword(e.target.value)}></input><br />
                <button id="loginButton" type="submit" >Login</button><br />
                <button id="registerButton" onClick={handleRedirectToRegister}> Not registered yet?</button>

            </form>
        </div>
        
    </div >)
}