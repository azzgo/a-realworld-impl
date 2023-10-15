import { getDefaultStore } from "jotai";
import { describe, beforeEach, test, expect } from "vitest";
import { useUserController, userAtom } from "../../src/model/user";
import { provider } from "./pact.utils";
import { configEnv } from "../../src/utils/env";
import { initAxiosInstance } from "../../src/utils/request";
import { renderHook } from "@testing-library/react";
import { getToken } from "../../src/utils/token";

describe("consumer test for register", () => {
  beforeEach(() => {
    localStorage.clear();
    getDefaultStore().set(userAtom, null);
  });

  test("when user is not registered", () => {
    provider
      .given("user not registered")
      .uponReceiving("a request to register")
      .withRequest({
        method: "POST",
        path: "/user",
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          user: {
            username: "jake",
            password: "jakejake",
            email: "jake@jake.jake",
          },
        },
      })
      .willRespondWith({
        status: 201,
        headers: { "Content-Type": "application/json" },
        body: {
          user: {
            email: "jake@jake.jake",
            token: "jwt.token.here",
            username: "jake",
            bio: "I work at statefarm",
            image: "http://image.url",
          },
        },
      });

    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController());

      await result.current.register("jake@jake.jake", "jake", "jakejake");
      expect(getDefaultStore().get(userAtom)).toEqual({
        email: "jake@jake.jake",
        username: "jake",
        bio: "I work at statefarm",
        image: "http://image.url",
      });
      expect(getToken()).toEqual("jwt.token.here");
    });
  });
});
