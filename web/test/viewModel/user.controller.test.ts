import { describe } from "vitest";
import { expect, test } from "vitest";
import { User, useUserController, userAtom } from "../../src/model/user";
import { createStore } from "jotai";
import { renderHook } from "@testing-library/react";
import { MockHeadlessStoreWrapper } from "../views/utils.utils";

describe("User Controller", () => {
  test("logout should clear token and user viewmodel", () => {
    localStorage.setItem("token", "token");
    const store = createStore();
    const wrapper: any = MockHeadlessStoreWrapper(store);
    const fakeUser: User = {
      email: "email",
      username: "username",
      bio: "bio",
      image: "image",
    };
    store.set(userAtom, fakeUser);

    const { result } = renderHook(() => useUserController(), { wrapper });
    result.current.logout();

    expect(localStorage.getItem("token")).toBeNull();
    expect(store.get(userAtom)).toBeNull();
  });
});
