import { Provider, getDefaultStore } from "jotai";
import React, { PropsWithChildren } from "react";
import { JotaiStore } from "../../src/type";

export function MockAppWrapper() {
  return function Wrapper({
    children,
    store = getDefaultStore(),
  }: PropsWithChildren<{ store: JotaiStore }>) {
    return (
      <React.StrictMode>
        <Provider store={store}>{children}</Provider>
      </React.StrictMode>
    );
  };
}
