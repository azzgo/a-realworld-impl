import { describe, expect, test } from "vitest";
import { MockAppWrapper } from "./utils.utils";
import { MemoryRouter, Route, Routes } from "react-router";
import React from "react";
import { fireEvent, render } from "@testing-library/react";
import Layout from "../../src/layout/Layout";

describe("AuthHeader Page", () => {
  test("can navigate to /editor to create articles", () => {
    const { getByTestId, queryByTestId } = renderAuthLayou();
    fireEvent.click(getByTestId("create-article"));
    expect(queryByTestId("editor")).not.toBeNull();
  });

  function renderAuthLayou() {
    const Wrapper = MockAppWrapper();
    return render(
      <Wrapper>
        <MemoryRouter initialEntries={["/"]}>
          <Routes>
            <Route path="/" element={<Layout />}>
              <Route path="/editor/:slug?" element={<div data-testid="editor" />} />
              <Route path="/" element={<div data-testid="home">home</div>} />
            </Route>
          </Routes>
        </MemoryRouter>
      </Wrapper>
    );
  }
});
