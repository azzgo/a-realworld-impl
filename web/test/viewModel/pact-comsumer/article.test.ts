import { describe, beforeEach, test, expect } from "vitest";

import { Article, useArticleController } from "../../../src/model/article";
import { JotaiStore } from "../../../src/type";
import { createStore } from "jotai";
import { MockHeadlessStoreWrapper } from "../../views/utils.utils";
import { renderHook } from "@testing-library/react";
import { jwtToken, provider } from "./pact.utils";
import { configEnv } from "../../../src/utils/env";
import { initAxiosInstance } from "../../../src/utils/request";
import { persistToken } from "../../../src/utils/token";
import { fakeArticles } from "../../helpers";

describe("consumer for create article", () => {
  let store: JotaiStore;
  let wrapper: any;

  beforeEach(() => {
    localStorage.clear();
    store = createStore();
    wrapper = MockHeadlessStoreWrapper(store);
  });

  test("should create article", async () => {
    persistToken(jwtToken);
    provider
      .given("user logged in what to post new article")
      .uponReceiving("a request to create article")
      .withRequest({
        method: "POST",
        path: "/articles",
        headers: {
          Authorization: `Token ${jwtToken}`,
          "Content-Type": "application/json",
        },
        body: {
          article: {
            title: "How to train your dragon",
            description: "Ever wonder how?",
            body: "You have to believe",
            tagList: ["reactjs", "angularjs", "dragons"],
          },
        },
      })
      .willRespondWith({
        status: 201,
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          article: {
            slug: "how-to-train-your-dragon",
            title: "How to train your dragon",
            description: "Ever wonder how?",
            body: "You have to believe",
            tagList: ["reactjs", "angularjs", "dragons"],
            createdAt: "2016-02-18T03:22:56.637Z",
            updatedAt: "2016-02-18T03:48:35.824Z",
            favorited: false,
            favoritesCount: 0,
            author: {
              username: "jake",
              bio: "I work at statefarm",
              image: "http://image.url",
              following: false,
            },
          },
        },
      });

    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useArticleController(), { wrapper });
      await result.current.create({
        title: "How to train your dragon",
        description: "Ever wonder how?",
        body: "You have to believe",
        tagList: ["reactjs", "angularjs", "dragons"],
      });
    });
  });

  test("should edit existing article", async () => {
    persistToken(jwtToken);
    provider
      .given("user logged in want to edit exist article")
      .uponReceiving("a request to edit article")
      .withRequest({
        method: "PUT",
        path: "/articles/slug",
        headers: {
          Authorization: `Token ${jwtToken}`,
          "Content-Type": "application/json",
        },
        body: {
          article: {
            title: "How to train your dragon",
            description: "Ever wonder how?",
            body: "You have to believe",
            tagList: ["reactjs", "angularjs", "dragons"],
          },
        },
      })
      .willRespondWith({
        status: 200,
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          article: {
            slug: "slug",
            title: "How to train your dragon",
            description: "Ever wonder how?",
            body: "You have to believe",
            tagList: ["reactjs", "angularjs", "dragons"],
            createdAt: "2016-02-18T03:22:56.637Z",
            updatedAt: "2016-02-18T03:48:35.824Z",
            favorited: false,
            favoritesCount: 0,
            author: {
              username: "jake",
              bio: "I work at statefarm",
              image: "http://image.url",
              following: false,
            },
          },
        },
      });
    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useArticleController(), { wrapper });
      await result.current.update("slug", {
        title: "How to train your dragon",
        description: "Ever wonder how?",
        body: "You have to believe",
        tagList: ["reactjs", "angularjs", "dragons"],
      });
    });
  });

  test("should get the article detail by slug id", () => {
    provider
      .given("get article by slug")
      .uponReceiving("a request to get article detail")
      .withRequest({
        method: "GET",
        path: "/articles/slug",
      })
      .willRespondWith({
        status: 200,
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          article: {
            slug: "slug",
            title: "How to train your dragon",
            description: "Ever wonder how?",
            body: "You have to believe",
            tagList: ["reactjs", "angularjs", "dragons"],
            createdAt: "2016-02-18T03:22:56.637Z",
            updatedAt: "2016-02-18T03:48:35.824Z",
            favorited: false,
            favoritesCount: 0,
            author: {
              username: "jake",
              bio: "I work at statefarm",
              image: "http://image.url",
              following: false,
            },
          },
        },
      });

    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useArticleController(), {
        wrapper,
      });
      const expectedArticle: Article = {
        slug: "slug",
        title: "How to train your dragon",
        description: "Ever wonder how?",
        body: "You have to believe",
        tagList: ["reactjs", "angularjs", "dragons"],
        createdAt: "2016-02-18T03:22:56.637Z",
        updatedAt: "2016-02-18T03:48:35.824Z",
        favorited: false,
        favoritesCount: 0,
        author: {
          username: "jake",
          bio: "I work at statefarm",
          image: "http://image.url",
          following: false,
        },
      };
      const article = await result.current.get("slug");
      expect(article).toEqual(expectedArticle);
    });
  });

  test("list articles pagination", async () => {
    provider
      .given("list articles default pagination")
      .uponReceiving("a request to list articles")
      .withRequest({
        method: "GET",
        path: "/articles",
        query: {
          offset: "0",
          limit: "10",
        },
      })
      .willRespondWith({
        status: 200,
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          articlesCount: 11,
          articles: [
            fakeArticles.articleLoremIpsum,
            fakeArticles.fakeArticleSitAmet,
            fakeArticles.articleAdipiscing,
            fakeArticles.articleConsectetur,
            fakeArticles.articleLorem1,
            fakeArticles.articleLorem2,
            fakeArticles.articleTheMagicOfMusic,
            fakeArticles.articleExploringTheGreatOutdoors,
            fakeArticles.articleTheArtOfCooking,
            fakeArticles.articleThePowerOfTheInternet,
          ],
        },
      });
    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useArticleController(), { wrapper });
      await result.current.list({
        pagination: { offset: 0, limit: 10 },
      });
    });
  });

  test("list articles by tag", () => {
    provider
      .given("list articles by tag")
      .uponReceiving("a request to list articles")
      .withRequest({
        method: "GET",
        path: "/articles",
        query: {
          tag: "lorem",
          offset: "0",
          limit: "20",
        },
      })
      .willRespondWith({
        status: 200,
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          articlesCount: 3,
          articles: [
            fakeArticles.articleLoremIpsum,
            fakeArticles.articleLorem1,
            fakeArticles.articleLorem2,
          ],
        },
      });
    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useArticleController(), { wrapper });
      await result.current.list({ tag: "lorem" });
    });
  });
  test.todo("list articles by author");
  test.todo("list articles by favorited");
  test.todo("list feed articles");
});
