import React from "react";
import { cleanup, fireEvent, render, waitFor } from "@testing-library/react";
import { beforeEach, describe, expect, test, vi } from "vitest";
import {
  UserController,
  UserControllerContext,
  userAtom,
} from "../../src/model/user";
import { MemoryRouter, Route, Routes } from "react-router";
import { MockAppWrapper } from "./utils.utils";
import Settings from "../../src/page/Settings";
import { createStore } from "jotai";
import { JotaiStore } from "../../src/type";

describe("Setting Page", () => {
  let mockUserController: UserController;
  let store: JotaiStore;
  beforeEach(() => {
    mockUserController = {
      logout: vi.fn(),
      update: vi.fn(),
    } as any;
    store = createStore();
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

  test("should render setting page of UserInfo", async () => {
    store.set(userAtom, {
      image: "http://image.com",
      username: "jake",
      bio: "bio",
      email: "jake@jake.jake",
    });

    const { queryByTestId } = renderSettingPage();

    await waitFor(() => {
      expect(
        (queryByTestId("profile-image") as HTMLInputElement).value
      ).toEqual("http://image.com");
      expect(
        (queryByTestId("profile-username") as HTMLInputElement).value
      ).toEqual("jake");
      expect(
        (queryByTestId("profile-bio") as HTMLTextAreaElement).value
      ).toEqual("bio");
      expect(
        (queryByTestId("profile-email") as HTMLInputElement).value
      ).toEqual("jake@jake.jake");
    });
  });

  test("should update user when click update Settings", async () => {
    const { queryByTestId } = renderSettingPage();

    fireEvent.change(queryByTestId("profile-image")!, {
      target: { value: "http://image.com" },
    });
    fireEvent.change(queryByTestId("profile-username")!, {
      target: { value: "jake" },
    });
    fireEvent.change(queryByTestId("profile-bio")!, {
      target: { value: "bio" },
    });
    fireEvent.change(queryByTestId("profile-email")!, {
      target: { value: "jake@jake.jake" },
    });
    fireEvent.change(queryByTestId("profile-password")!, {
      target: { value: "password" },
    });

    const updateBtn = queryByTestId("update-btn");
    expect(updateBtn).not.toBeNull();

    fireEvent.click(updateBtn!);

    await waitFor(() => {
      expect(mockUserController.update).toBeCalledWith({
        image: "http://image.com",
        username: "jake",
        bio: "bio",
        email: "jake@jake.jake",
        password: "password",
      });
    });
  });

  function renderSettingPage() {
    const Wrapper = MockAppWrapper();

    return render(
      <Wrapper store={store}>
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
