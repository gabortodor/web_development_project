import {configureStore} from '@reduxjs/toolkit'
import userReducer from "./UserSlice.js";

export default configureStore( {
    reducer: {
        user: userReducer
    },
});