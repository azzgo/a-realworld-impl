import { beforeEach, describe, expect, test } from "vitest";
import { renderHook } from "@testing-library/react";
import { provider } from "./pact.utils";
import { configEnv } from "../../src/utils/env";
import { useUserController, userAtom } from "../../src/model/user";
import { initAxiosInstance } from "../../src/utils/request";
import { getDefaultStore } from "jotai";
import { getToken } from "../../src/utils/token";
import { AxiosError } from "axios";

describe("comsumer test for login", () => {
  beforeEach(() => {
    localStorage.clear();
    getDefaultStore().set(userAtom, null);
  });
  test("when login succuess should update store and token", async () => {
    provider
      .given("user exists")
      .uponReceiving("a request to login")
      .withRequest({
        method: "POST",
        path: "/user/login",
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          user: {
            email: "jake@jake.jake",
            password: "jakejake",
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
      await result.current.login("jake@jake.jake", "jakejake");
      expect(getDefaultStore().get(userAtom)).toEqual({
        email: "jake@jake.jake",
        username: "jake",
        bio: "I work at statefarm",
        image: "http://image.url",
      });
      expect(getToken()).toEqual("jwt.token.here");
    });
  });

  test("when login failed should not update store and token and throw error", async () => {
    provider
      .given("user not or password invalid")
      .uponReceiving("a request to login")
      .withRequest({
        method: "POST",
        path: "/user/login",
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          user: {
            email: "fake@jake.jake",
            password: "fakefake",
          },
        },
      })
      .willRespondWith({
        status: 403,
        body: {
          errors: {
            "email or password": ["is invalid"],
          },
        },
      });
    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController());

      const capturedError: AxiosError = await result.current
        .login("fake@jake.jake", "fakefake")
        .catch((e) => e);
      expect(capturedError.response?.status).toEqual(403);
      expect(capturedError.response?.data).toEqual({
        errors: {
          "email or password": ["is invalid"],
        },
      });

      expect(getDefaultStore().get(userAtom)).toEqual(null);
      expect(getToken()).toEqual(null);
    });
  });
});
