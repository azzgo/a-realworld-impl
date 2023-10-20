import { createContext } from "react";
import request from "../utils/request";

export type Slug = string;

export interface ArticleDto {
  title: string;
  description: string;
  body: string;
  tagList: string[];
}

export interface Article {
  slug: Slug;
  title: string;
  description: string;
  body: string;
  tagList: string[];
  createdAt: string;
  updatedAt: string;
  favorited: boolean;
  favoritesCount: number;
  author: Author;
}

export interface Author {
  username: string;
  bio: string;
  image: string;
  following: boolean;
}

type SingleAritleResponse = {
  article: Article
}

export interface ArticleController {
  create(article: ArticleDto): Promise<Article>;
  update(slug: Slug, article: ArticleDto): Promise<Article>;
  get(slug: Slug): Promise<Article>;
}

export function useArticleController(): ArticleController {
  return {
    async create(article: ArticleDto) {
      return request().post<SingleAritleResponse>("/articles", { article }).then(res => res.data.article);
    },
    async update(slug: Slug, article: ArticleDto) {
      return request().put<SingleAritleResponse>(`/articles/${slug}`, { article }).then(res => res.data.article);
    },
    async get(slug: Slug) {
      return request().get<SingleAritleResponse>(`/articles/${slug}`).then(res => res.data.article);
    }
  };
}

export const ArticleControllerContext = createContext<ArticleController | null>(
  null
);
