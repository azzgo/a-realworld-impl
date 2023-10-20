import { beforeEach, describe, expect, test, vi } from "vitest";
import { MockAppWrapper } from "./utils.utils";
import { fireEvent, render, waitFor } from "@testing-library/react";
import { JotaiStore } from "../../src/type";
import { createStore } from "jotai";
import ArticleEditor from "../../src/page/ArticleEditor";
import {
  ArticleController,
  ArticleControllerContext,
} from "../../src/model/article";
import React from "react";
import { MemoryRouter, Route, Routes } from "react-router-dom";

describe("ArticleEditor", () => {
  let store: JotaiStore;
  let articleController: ArticleController;
  beforeEach(() => {
    store = createStore();
    articleController = {
      create: vi.fn().mockResolvedValue(undefined),
    } as any;
  });
  test("should create article when click publish", async () => {
    const { getByTestId } = renderArticlePage();

    fireEvent.input(getByTestId("article-title"), {
      target: { value: "title" },
    });
    fireEvent.input(getByTestId("article-description"), {
      target: { value: "description" },
    });
    fireEvent.input(getByTestId("article-body"), { target: { value: "body" } });

    fireEvent.click(getByTestId("publish-button"));

    await waitFor(() => {
      expect(articleController.create).toBeCalledWith({
        title: "title",
        description: "description",
        body: "body",
        tagList: [],
      });
    });
  });

  function renderArticlePage(isEdit = false) {
    const Wrapper = MockAppWrapper();

    return render(
      <Wrapper store={store}>
        <ArticleControllerContext.Provider value={articleController}>
          <MemoryRouter initialEntries={[isEdit ? "/editor/slug" : "/editor"]}>
            <Routes>
              <Route path="/editor" element={<ArticleEditor />} />
              <Route path="/editor/:slug" element={<ArticleEditor />} />
            </Routes>
          </MemoryRouter>
        </ArticleControllerContext.Provider>
      </Wrapper>
    );
  }
});
