import { Outlet } from "react-router-dom";
import Footer from "./components/Footer";
import UnauthenticatedHeader from "./components/UnauthenticatedHeader";
import AuthenticatedHeader from "./components/AuthenticatedHeader";
import {useIsLogin} from "../model/user";

export default function Layout() {
  const isLogin = useIsLogin();
  return (
    <>
      { isLogin ? <AuthenticatedHeader /> : <UnauthenticatedHeader /> }
      <div>
        <Outlet />
      </div>
      <Footer />
    </>
  );
}
