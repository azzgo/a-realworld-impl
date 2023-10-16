import { Provider, createStore } from "jotai";
import React, { PropsWithChildren } from "react";
import { JotaiStore } from "../../src/type";

export function MockAppWrapper() {
  return function Wrapper({
    children,
    store = createStore(),
  }: PropsWithChildren<{ store?: JotaiStore }>) {
    return (
      <>
        <Provider store={store}>{children}</Provider>
      </>
    );
  };
}

export function MockHeadlessStoreWrapper(
    store = createStore(),
) {
  return function Wrapper({
    children,
  }: PropsWithChildren<{ store: JotaiStore }>) {
    return (
      <>
        <Provider store={store}>{children}</Provider>
      </>
    );
  };
}
