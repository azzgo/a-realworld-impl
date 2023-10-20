import { beforeEach, describe, expect, test, vi } from "vitest";
import { MockAppWrapper } from "./utils.utils";
import { cleanup, fireEvent, render, waitFor } from "@testing-library/react";
import { JotaiStore } from "../../src/type";
import { createStore } from "jotai";
import ArticleEditor from "../../src/page/ArticleEditor";
import {
  Article,
  ArticleController,
  ArticleControllerContext,
} from "../../src/model/article";
import React from "react";
import { MemoryRouter, Route, Routes } from "react-router-dom";

const emptyArticle: Article = {
  slug: "",
  title: "",
  description: "",
  body: "",
  tagList: [],
  createdAt: "",
  updatedAt: "",
  favorited: false,
  favoritesCount: 0,
  author: {
    username: "",
    bio: "",
    image: "",
    following: false,
  },
};

describe("ArticleEditor", () => {
  let store: JotaiStore;
  let articleController: ArticleController;
  beforeEach(() => {
    store = createStore();
    articleController = {
      create: vi.fn().mockResolvedValue(undefined),
      update: vi.fn().mockResolvedValue(undefined),
      get: vi.fn().mockResolvedValue(emptyArticle),
    } as any;
    cleanup();
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

  test("should update article when article exists", async () => {
    const { getByTestId } = renderArticlePage(true);

    fireEvent.input(getByTestId("article-title"), {
      target: { value: "title" },
    });
    fireEvent.input(getByTestId("article-description"), {
      target: { value: "description" },
    });
    fireEvent.input(getByTestId("article-body"), { target: { value: "body" } });
    fireEvent.click(getByTestId("publish-button"));
    await waitFor(() => {
      expect(articleController.update).toBeCalledWith("slug", {
        title: "title",
        description: "description",
        body: "body",
        tagList: [],
      });
    });
  });

  test("should get article when load page", async () => {
    const expectedArticle: Article = {
      slug: "slug",
      title: "title",
      description: "description",
      body: "body",
      tagList: ["reactjs", "angularjs"],
      createdAt: "",
      updatedAt: "",
      favorited: false,
      favoritesCount: 0,
      author: {
        username: "",
        bio: "",
        image: "",
        following: false,
      },
    };
    (articleController.get as any).mockResolvedValue(expectedArticle);

    const { getByTestId } = renderArticlePage(true);
    await waitFor(() => {
      expect((getByTestId("article-title") as any).value).toBe("title");
      expect((getByTestId("article-description") as any).value).toBe(
        "description"
      );
      expect((getByTestId("article-body") as any).value).toBe("body");
      expect(getByTestId("tag-list").textContent).toContain("reactjs");
      expect(getByTestId("tag-list").textContent).toContain("angularjs");
    });
  });

  function renderArticlePage(isEdit = false) {
    const Wrapper = MockAppWrapper();

    return render(
      <Wrapper store={store}>
        <ArticleControllerContext.Provider value={articleController}>
          <MemoryRouter initialEntries={[isEdit ? "/editor/slug" : "/editor"]}>
            <Routes>
              <Route path="/editor/:slug?" element={<ArticleEditor />} />
            </Routes>
          </MemoryRouter>
        </ArticleControllerContext.Provider>
      </Wrapper>
    );
  }
});
