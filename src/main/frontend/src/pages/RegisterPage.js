import React, { useState } from 'react';
import axios from "axios";
import { Helmet } from "react-helmet";

export function RegisterPage(props) {


    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");


    function handleSubmit(event) {
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/user/register`,
            'data': {
                email: email,
                username: username,
                password: password,
                roles: ["ROLE_CLIENT"]
            }
        }).then(() => {
            props.history.push("/login");
        }).catch(function (error) {
            if (error.response) {
                alert(error.response.data);
            }
        })
        event.preventDefault();
    }

    function handleRedirectToLogin(event) {
        props.history.push("/login")
    }

    return (<div className="registerpage">
        <Helmet>
            <meta charSet="UTF-8" />
            <title>Register</title>
        </Helmet>
        <div className="register">
            <form onSubmit={handleSubmit} method="POST">
                <label><b>Email</b></label><br />
                <input name="email" type="email" onChange={e => setEmail(e.target.value)}></input><br />
                <label><b>Username</b></label><br />
                <input name="username" type="text" onChange={e => setUsername(e.target.value)}></input><br />
                <label><b>Password</b></label><br />
                <input name="password" type="password" onChange={e => setPassword(e.target.value)}></input><br />
                <button id="registerButton" type="submit" >Register</button><br />
                <button id="loginButton" onClick={handleRedirectToLogin}> Already have an account?</button>

            </form>
        </div>
    </div>)
}