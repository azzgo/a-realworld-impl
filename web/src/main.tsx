import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import { createStore, Provider } from "jotai";
import React from "react";
import { useUserController, UserControllerContext } from "./model/user";
import {
  ArticleControllerContext,
  useArticleController,
} from "./model/article";

const appStore = createStore();

function Wrapper() {
  const userController = useUserController();
  const articleController = useArticleController();
  return (
    <React.StrictMode>
      <Provider store={appStore}>
        <UserControllerContext.Provider value={userController}>
          <ArticleControllerContext.Provider value={articleController}>
            <App />
          </ArticleControllerContext.Provider>
        </UserControllerContext.Provider>
      </Provider>
    </React.StrictMode>
  );
}

ReactDOM.createRoot(document.getElementById("root")!).render(<Wrapper />);
