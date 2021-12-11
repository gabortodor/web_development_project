import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import {Provider} from 'react-redux';
import {BrowserRouter,Routes, Route} from "react-router-dom";
import {LoginPage} from "./pages/LoginPage";
import store from './redux/store';

ReactDOM.render(
  <Provider store={store}>
      <BrowserRouter>
          <Routes>
              <Route path="/login" component={LoginPage}/>
          </Routes>
      </BrowserRouter>
  </Provider>,
  document.getElementById("root")
);
