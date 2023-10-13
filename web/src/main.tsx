import React from "react";
import ReactDOM from "react-dom/client";
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
import { Provider } from "jotai";
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
      { path: "/", loader: simpleGuard, element: <Home /> },
    ],
  },
]);

function App() {
  const userController = useUserController(); 
  return (
    <React.StrictMode>
      <Provider>
        <UserControllerContext.Provider value={userController} >
          <RouterProvider router={routes} />
        </UserControllerContext.Provider>
      </Provider>
    </React.StrictMode>
  );
}

ReactDOM.createRoot(document.getElementById("root")!).render(<App />);
