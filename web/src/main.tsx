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
import advancedFormat from "dayjs/plugin/advancedFormat";
import dayjs from "dayjs";

dayjs.extend(advancedFormat);

const appStore = createStore();

// eslint-disable-next-line react-refresh/only-export-components
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
