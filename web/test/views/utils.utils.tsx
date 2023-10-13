import { Provider } from "jotai";
import React, { PropsWithChildren } from "react";

export function MockAppWrapper() {
  return function Wrapper({ children }: PropsWithChildren<{}>) {
    return (
      <React.StrictMode>
        <Provider>{children}</Provider>
      </React.StrictMode>
    );
  };
}
