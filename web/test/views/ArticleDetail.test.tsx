import { beforeEach, describe, test, vi } from "vitest";
import { MockAppWrapper } from "./utils.utils";
import { cleanup, render } from "@testing-library/react";
import { JotaiStore } from "../../src/type";
import { createStore } from "jotai";
import {
  ArticleController,
  ArticleControllerContext,
} from "../../src/model/article";
import React from "react";
import ArticleDetail from "../../src/page/ArticleDetail";
import { MemoryRouter, Route, Routes } from "react-router-dom";

describe("ArticleDetail", () => {
  let store: JotaiStore;
  let articleController: ArticleController;
  beforeEach(() => {
    store = createStore();
    articleController = {
      get: vi.fn(),
    } as any;
    cleanup();
  });

  test.todo("should load article to view")

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  function renderArticleDetail() {
    const Wrapper = MockAppWrapper();

    return render(
      <Wrapper store={store}>
        <ArticleControllerContext.Provider value={articleController}>
          <MemoryRouter initialEntries={["/article/slug"]}>
            <Routes>
              <Route path="/article/:slug" element={<ArticleDetail />} />
            </Routes>
          </MemoryRouter>
        </ArticleControllerContext.Provider>
      </Wrapper>
    );
  }
});
