import { createContext } from "react";
import request from "../utils/request";

export interface Article {
  title: string;
  description: string;
  body: string;
  tagList: string[];
}

export interface ArticleController {
  create(article: Article): Promise<Article>;
}

export function useArticleController(): ArticleController {
  return {
    async create(article: Article) {
      return request().post("/articles", { article });
    },
  };
}

export const ArticleControllerContext = createContext<ArticleController | null>(
  null
);
