import { describe } from "node:test";
import { expect, test } from "vitest";
import { User, useUserController, userAtom } from "../../src/model/user";
import { getDefaultStore } from "jotai";
import { renderHook } from "@testing-library/react";

describe("User Controller", () => {
  test("logout should clear token and user viewmodel", () => {
    localStorage.setItem("token", "token");
    const fakeUser: User = {
      email: "email",
      username: "username",
      bio: "bio",
      image: "image",
    };
    getDefaultStore().set(userAtom, fakeUser);

    const { result } = renderHook(() => useUserController());
    result.current.logout();

    expect(localStorage.getItem("token")).toBeNull();
    expect(getDefaultStore().get(userAtom)).toBeNull();
  });
});
