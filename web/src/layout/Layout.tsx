import { Outlet } from "react-router-dom";
import Footer from "./components/Footer";
import UnauthenticatedHeader from "./components/UnauthenticatedHeader";
import { getToken } from "../utils/token";
import AuthenticatedHeader from "./components/AuthenticatedHeader";

export default function Layout() {
  return (
    <>
      { getToken() == null ? <UnauthenticatedHeader /> : <AuthenticatedHeader /> }
      <div>
        <Outlet />
      </div>
      <Footer />
    </>
  );
}
