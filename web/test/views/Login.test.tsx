import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import Login from "../../src/page/Login";
import React from "react";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { UserController, UserControllerContext } from "../../src/model/user";
import { MockAppWrapper } from "./utils.utils";
import { MemoryRouter, Route, Routes } from "react-router";

describe("Login component", () => {
  let mockUserController: UserController;
  beforeEach(() => {
    mockUserController = {
      login: vi.fn(),
    };
  });
  test("should call login and redirect to home page when summit email and password", () => {
    (mockUserController.login as any).mockResolvedValueOnce(undefined);

    const { getByTestId } = renderLogin();
    const emailInput = getByTestId("email-input");
    const passwordInput = getByTestId("password-input");
    fireEvent.change(emailInput, { target: { value: "jake@jake.jake" } });
    fireEvent.change(passwordInput, { target: { value: "jakejake" } });
    const submitButton = getByTestId("submit-button");
    fireEvent.click(submitButton);

    waitFor(() => {
      expect(mockUserController.login).toHaveBeenCalledWith(
        "jake@jake.jake",
        "jakejake"
      );
      expect(getByTestId("home")).toBeDefined();
    });
  });

  function renderLogin() {
    const Wrapper = MockAppWrapper();

    return render(
      <Wrapper>
        <UserControllerContext.Provider value={mockUserController}>
          <MemoryRouter initialEntries={["/login"]}>
            <Routes>
              <Route element={<Login />} path="/login" />
              <Route element={<div date-testid="home">Home</div>} path="/" />
            </Routes>
          </MemoryRouter>
        </UserControllerContext.Provider>
      </Wrapper>
    );
  }
});
