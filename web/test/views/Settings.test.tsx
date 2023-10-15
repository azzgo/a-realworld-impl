import React from "react";
import { cleanup, fireEvent, render, waitFor } from "@testing-library/react";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { UserController, UserControllerContext } from "../../src/model/user";
import { MemoryRouter, Route, Routes } from "react-router";
import { MockAppWrapper } from "./utils.utils";
import Settings from "../../src/page/Settings";

describe("Setting Page", () => {
  let mockUserController: UserController;
  beforeEach(() => {
    mockUserController = {
      logout: vi.fn(),
    } as any;
    cleanup();
  });

  test("should logout and redirect to home page", async () => {
    const { queryByTestId } = renderSettingPage();
    const logoutBtn = queryByTestId("logout-btn");
    expect(logoutBtn).not.toBeNull();

    fireEvent.click(logoutBtn!);

    await waitFor(() => {
      expect(mockUserController.logout).toHaveBeenCalled();
      expect(queryByTestId("home")).not.toBeNull();
    });
  });

  function renderSettingPage() {
    const Wrapper = MockAppWrapper();

    return render(
      <Wrapper>
        <UserControllerContext.Provider value={mockUserController}>
          <MemoryRouter initialEntries={["/settings"]}>
            <Routes>
              <Route element={<Settings />} path="/settings" />
              <Route element={<div data-testid="home">Home</div>} path="/" />
            </Routes>
          </MemoryRouter>
        </UserControllerContext.Provider>
      </Wrapper>
    );
  }
});
