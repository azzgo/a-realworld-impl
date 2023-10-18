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
import { getToken, hasToken } from "./utils/token.ts";
import Settings from "./page/Settings.tsx";
import { useContext, useEffect, useState } from "react";
import { UserControllerContext } from "./model/user.ts";
import ArticleEditor from "./page/ArticleEditor.tsx";

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
      { path: "/settings", loader: simpleGuard, element: <Settings /> },
      { path: "/editor", loader: simpleGuard, element: <ArticleEditor /> }
    ],
  },
]);

export default function App() {
  const userController = useContext(UserControllerContext);
  const [isLoaded, setIsLoaded] = useState(false);
  useEffect(() => {
    if (hasToken() && !isLoaded) {
      userController?.loadCurrentUser().then(() => setIsLoaded(true));
    } else {
      setIsLoaded(true);
    }
  }, []);

  return isLoaded && <RouterProvider router={routes} />;
}
