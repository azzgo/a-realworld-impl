import { fireEvent, render, waitFor } from "@testing-library/react";
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
    } as any;
  });
  test("should call login and redirect to home page when summit email and password", async () => {
    (mockUserController.login as any).mockResolvedValueOnce(undefined);

    const { getByTestId, queryByTestId } = renderLogin();
    const emailInput = getByTestId("email-input");
    const passwordInput = getByTestId("password-input");
    fireEvent.change(emailInput, { target: { value: "jake@jake.jake" } });
    fireEvent.change(passwordInput, { target: { value: "jakejake" } });
    const submitButton = getByTestId("submit-button");
    fireEvent.click(submitButton);

    await waitFor(
      () => {
        expect(mockUserController.login).toHaveBeenCalledWith(
          "jake@jake.jake",
          "jakejake"
        );
        expect(queryByTestId("home")).not.toBeNull();
      },
      { timeout: 2000 }
    );
  });

  test("should display error message when login failed", () => {
    (mockUserController.login as any).mockResolvedValueOnce(undefined);

    const { getByTestId, queryByTestId } = renderLogin();
    expect(queryByTestId("error-messages")).toBeNull();

    const emailInput = getByTestId("email-input");
    const passwordInput = getByTestId("password-input");
    fireEvent.change(emailInput, { target: { value: "fake@jake.jake" } });
    fireEvent.change(passwordInput, { target: { value: "fakefake" } });
    const submitButton = getByTestId("submit-button");
    fireEvent.click(submitButton);
    waitFor(() => {
      expect(getByTestId("error-messages")).toBeDefined();
      expect(getByTestId("error-messages").textContent).toEqual(
        "email or password is invalid"
      );
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
              <Route element={<div data-testid="home">Home</div>} path="/" />
            </Routes>
          </MemoryRouter>
        </UserControllerContext.Provider>
      </Wrapper>
    );
  }
});
