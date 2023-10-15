import React from "react";
import { fireEvent, render, waitFor } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router";
import { beforeEach, describe, expect, vi, test } from "vitest";
import { UserController, UserControllerContext } from "../../src/model/user";
import { MockAppWrapper } from "./utils.utils";
import Register from "../../src/page/Register";

describe("Register Page", () => {

  let mockUserController: UserController;
  beforeEach(() => {
    mockUserController = {
      register: vi.fn(),
    } as any;
  });
  test("should regiest new user", async () => {
    (mockUserController.register as any).mockResolvedValueOnce(undefined);

    const { getByTestId } = renderRegisterPage();
    const usernameInput = getByTestId("username-input");
    const emailInput = getByTestId("email-input");
    const passwordInput = getByTestId("password-input");
    fireEvent.change(usernameInput, { target: { value: "jake" } });
    fireEvent.change(emailInput, { target: { value: "jake@jake.jake" } });
    fireEvent.change(passwordInput, { target: { value: "jakejake" } });
    const submitButton = getByTestId("submit-button");
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(mockUserController.register).toHaveBeenCalledWith(
        "jake@jake.jake",
        "jake",
        "jakejake"
      );
      expect(getByTestId("home")).toBeDefined();
    }, { timeout: 2000 });
  });

  test("should show error tip to user when user username already exist", async () => {
    (mockUserController.register as any).mockRejectedValueOnce({
      response: {
        data: {
          errors: {
            username: ["has already been taken"],
          },
        },
      },
    });

    const { getByTestId, queryByTestId } = renderRegisterPage();
    expect(queryByTestId("error-messages")).toBeNull();
    const usernameInput = getByTestId("username-input");
    const emailInput = getByTestId("email-input");
    const passwordInput = getByTestId("password-input");
    fireEvent.change(usernameInput, { target: { value: "jake" } });
    fireEvent.change(emailInput, { target: { value: "jaek@jake.jake" } });
    fireEvent.change(passwordInput, { target: { value: "jakejake" } });

    const submitButton = getByTestId("submit-button");
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(getByTestId("error-messages")).toBeDefined();
      expect(getByTestId("error-messages").textContent).toEqual(
        "username has already been taken"
      );
    });
  });
  test.todo("should show error tip to user when user email already exist", async () => {});

  function renderRegisterPage() {
    const Wrapper = MockAppWrapper();

    return render(
      <Wrapper>
        <UserControllerContext.Provider value={mockUserController}>
          <MemoryRouter initialEntries={["/register"]}>
            <Routes>
              <Route element={<Register />} path="/register" />
              <Route element={<div data-testid="home">Home</div>} path="/" />
            </Routes>
          </MemoryRouter>
        </UserControllerContext.Provider>
      </Wrapper>
    );
  }
});
