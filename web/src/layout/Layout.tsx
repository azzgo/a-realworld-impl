import { Outlet } from "react-router-dom";
import Footer from "./components/Footer";
import UnauthenticatedHeader from "./components/UnauthenticatedHeader";
import { hasToken } from "../utils/token";
import AuthenticatedHeader from "./components/AuthenticatedHeader";

export default function Layout() {
  return (
    <>
      { hasToken() ? <UnauthenticatedHeader /> : <AuthenticatedHeader /> }
      <div>
        <Outlet />
      </div>
      <Footer />
    </>
  );
}
