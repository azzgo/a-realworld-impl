import { beforeEach, describe, expect, test, vi } from "vitest";
import { MockAppWrapper } from "./utils.utils";
import { cleanup, render, waitFor } from "@testing-library/react";
import { JotaiStore } from "../../src/type";
import { createStore } from "jotai";
import {
  Article,
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

  test("should load article to view", async () => {
    const expectedArticle: Article = {
      slug: "slug",
      title: "How to build webapps that scale",
      description:
        "Web development technologies have evolved at an incredible clip over the past few years.",
      body: "##Introducing RealWorld.\nIt's a great solution for learning how other frameworks work.",
      tagList: ["realworld", "implementations"],
      favoritesCount: 29,
      favorited: false,
      createdAt: "2020-01-20T00:00:00",
      updatedAt: "2020-01-20T00:00:00",
      author: {
        username: "EricSimons",
        bio: "Eric Simons is a software engineer based in New York City.",
        image: "http://i.imgur.com/Qr71crq.jpg",
        following: false,
      },
    };

    (articleController.get as any).mockReturnValueOnce(expectedArticle);

    const { getByTestId, getAllByTestId } = renderArticleDetail();

    await waitFor(() => {
      expect(articleController.get).toBeCalledWith("slug");

      expect(getByTestId("article-title").textContent).toEqual(
        expectedArticle.title
      );
      expect(getByTestId("article-description").textContent).toEqual(
        expectedArticle.description
      );
      expect(getByTestId("article-content").innerHTML).toContain(
        "<h2>Introducing RealWorld.</h2>"
      );
      expect(getByTestId("article-content").innerHTML).toContain(
        "<p>It's a great solution for learning how other frameworks work.</p>"
      );
      expect(getByTestId("article-tag-list").textContent).toContain(
        "realworld"
      );
      expect(getByTestId("article-tag-list").textContent).toContain(
        "implementations"
      );
      expect(getByTestId("article-updatedAt").textContent).toEqual(
        "January 20th"
      );
      expect(getAllByTestId("article-favoritesCount").length).toEqual(2);
      expect(
        getAllByTestId("article-favoritesCount").every((it) =>
          it.textContent?.includes("29")
        )
      ).toBeTruthy();
    });
  });

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
