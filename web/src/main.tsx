import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import { createStore, Provider } from "jotai";
import React from "react";
import { useUserController, UserControllerContext } from "./model/user";

const appStore = createStore();

function Wrapper() {
  const userController = useUserController();
  return (
    <React.StrictMode>
      <Provider store={appStore}>
        <UserControllerContext.Provider value={userController}>
          <App />
        </UserControllerContext.Provider>
      </Provider>
    </React.StrictMode>
  );
}

ReactDOM.createRoot(document.getElementById("root")!).render(<Wrapper />);
