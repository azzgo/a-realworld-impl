import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import { createStore, getDefaultStore, Provider, useAtom } from "jotai";
import React from "react";
import {
  useUserController,
  UserControllerContext,
  userAtom,
} from "./model/user";
import {
  ArticleControllerContext,
  useArticleController,
} from "./model/article";
import advancedFormat from "dayjs/plugin/advancedFormat";
import dayjs from "dayjs";
import { configEnv } from "./utils/env";
import { initAxiosInstance } from "./utils/request";
import { DevTools } from "jotai-devtools";

dayjs.extend(advancedFormat);
configEnv({
  BASE_URL: "/api",
});

const appStore = createStore();

initAxiosInstance(appStore);

// eslint-disable-next-line react-refresh/only-export-components
function Wrapper() {
  return (
    <React.StrictMode>
      <Provider store={appStore}>
        <DevTools store={appStore} />
            <App />
      </Provider>
    </React.StrictMode>
  );
}

ReactDOM.createRoot(document.getElementById("root")!).render(<Wrapper />);
