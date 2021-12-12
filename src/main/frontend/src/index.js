import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {LoginPage} from "./pages/LoginPage";
import {RegisterPage} from "./pages/RegisterPage";
import {HomePage} from "./pages/HomePage";
import "./pages/homepage.scss"
import "./pages/loginpage.scss"


ReactDOM.render(
      <BrowserRouter>
          <Switch>
              <Route exact path="/login" component={LoginPage}/>
              <Route exact path="/register" component={RegisterPage}/>
              <Route exact path="/home" component={HomePage}/>
              <Route exact path="/" component={LoginPage}/>
          </Switch>
      </BrowserRouter>,
  document.getElementById("root")
);
