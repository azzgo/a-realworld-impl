import React from "react";
import ReactDOM from "react-dom/client";
import Home from "./pages/Home.tsx";
import { createBrowserRouter, redirect, RouterProvider } from "react-router-dom";
import "./index.css";
import Login from "./pages/Login.tsx";
import Register from "./pages/Register.tsx";
import Layout from "./layout/Layout.tsx";
import { getToken } from "./utils/token.ts";


const simpleGuard = () => {
  if (getToken() == null) {
    return redirect("/login");
  }
  return null
}

const routes = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      { path: "/login", element:  <Login /> },
      { path: "/register", element: <Register /> },
      { path: "/", loader: simpleGuard, element: <Home /> },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <RouterProvider router={routes} />
  </React.StrictMode>
);
