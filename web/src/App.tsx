import React from "react";
import Home from "./page/Home.tsx";
import {
  createBrowserRouter,
  redirect,
  RouterProvider,
} from "react-router-dom";
import "./index.css";
import Login from "./page/Login.tsx";
import Register from "./page/Register.tsx";
import Layout from "./layout/Layout.tsx";
import { getToken } from "./utils/token.ts";
import { createStore, Provider } from "jotai";
import { UserControllerContext, useUserController } from "./model/user.ts";

const simpleGuard = () => {
  if (getToken() == null) {
    return redirect("/login");
  }
  return null;
};

const routes = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      { path: "/login", element: <Login /> },
      { path: "/register", element: <Register /> },
      { path: "/", element: <Home /> },
      { path: "/settings", loader: simpleGuard, element: <Home /> },
    ],
  },
]);

const appStore = createStore()

export default function App() {
  const userController = useUserController(); 
  return (
    <React.StrictMode>
      <Provider store={appStore}>
        <UserControllerContext.Provider value={userController} >
          <RouterProvider router={routes} />
        </UserControllerContext.Provider>
      </Provider>
    </React.StrictMode>
  );
}
