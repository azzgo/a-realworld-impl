import { describe, expect, test } from "vitest";
import { MockAppWrapper } from "./utils-wrapper";
import { MemoryRouter, Route, Routes } from "react-router";
import React from "react";
import { fireEvent, render } from "@testing-library/react";
import Layout from "../../src/layout/Layout";
import { createStore } from "jotai";
import { fakeLoginUser } from "./utils-tools";

describe("AuthHeader Page", () => {
  test("can navigate to /editor to create articles", () => {
    const { getByTestId, queryByTestId } = renderAuthLayou();
    fireEvent.click(getByTestId("create-article"));
    expect(queryByTestId("editor")).not.toBeNull();
  });

  function renderAuthLayou() {
    const store = createStore();
    fakeLoginUser(store);
    const Wrapper = MockAppWrapper();
    return render(
      <Wrapper store={store}>
        <MemoryRouter initialEntries={["/"]}>
          <Routes>
            <Route path="/" element={<Layout />}>
              <Route
                path="/editor/:slug?"
                element={<div data-testid="editor" />}
              />
              <Route path="/" element={<div data-testid="home">home</div>} />
            </Route>
          </Routes>
        </MemoryRouter>
      </Wrapper>
    );
  }
});
