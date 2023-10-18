import { createContext } from "react";

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
            throw new Error("Not implemented");
        },
    };
}

export const ArticleControllerContext = createContext<ArticleController | null>(null);
