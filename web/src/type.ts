import { createStore } from "jotai";

export interface ErrorBody {
  errors: {
    [key: string]: string[];
  };
}

export type JotaiStore = ReturnType<typeof createStore>;