import { createStore } from "jotai";

export interface ErrorBody {
  errors: {
    [key: string]: string[];
  };
}

export interface Pagination {
  limit: number;
  offset: number;
}

export type JotaiStore = ReturnType<typeof createStore>;
