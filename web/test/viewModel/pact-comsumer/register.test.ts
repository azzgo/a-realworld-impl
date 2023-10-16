import { createStore } from "jotai";
import { describe, beforeEach, test, expect } from "vitest";
import { useUserController, userAtom } from "../../../src/model/user";
import { provider } from "./pact.utils";
import { configEnv } from "../../../src/utils/env";
import { initAxiosInstance } from "../../../src/utils/request";
import { renderHook } from "@testing-library/react";
import { getToken } from "../../../src/utils/token";
import { AxiosError } from "axios";
import { JotaiStore } from "../../../src/type";
import { MockHeadlessStoreWrapper } from "../../views/utils.utils";

describe("consumer test for register", () => {
  let store: JotaiStore
  let wrapper: any;
  beforeEach(() => {
    localStorage.clear();
    store = createStore();
    wrapper = MockHeadlessStoreWrapper(store);
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
          },
        },
      });

    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController(), { wrapper });

      await result.current.register("jake@jake.jake", "jake", "jakejake");
      expect(store.get(userAtom)).toEqual({
        email: "jake@jake.jake",
        username: "jake",
      });
      expect(getToken()).toEqual("jwt.token.here");
    });
  });

  test("when register username already exist", async () => {
    provider
      .given("username already exist when registering")
      .uponReceiving("a request to register")
      .withRequest({
        method: "POST",
        path: "/user",
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          user: {
            email: "jake@jake.jake",
            username: "jake_exist",
            password: "fakefake",
          },
        },
      })
      .willRespondWith({
        status: 422,
        body: {
          errors: {
            username: ["has already been taken"],
          },
        },
      });
    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController(), { wrapper });

      const capturedError: AxiosError = await result.current
        .register("jake@jake.jake", "jake_exist", "fakefake")
        .catch((e) => e);
      expect(capturedError.response?.status).toEqual(422);
      expect(capturedError.response?.data).toEqual({
        errors: {
          username: ["has already been taken"],
        },
      });

      expect(store.get(userAtom)).toEqual(null);
      expect(getToken()).toEqual(null);
    });
  });

  test("when register email already exist", async () => {
    provider
      .given("email already exist when registering")
      .uponReceiving("a request to register")
      .withRequest({
        method: "POST",
        path: "/user",
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          user: {
            email: "jake@jake.taken",
            username: "jake",
            password: "fakefake",
          },
        },
      })
      .willRespondWith({
        status: 422,
        body: {
          errors: {
            email: ["has already been taken"],
          },
        },
      });
    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController(), { wrapper });

      const capturedError: AxiosError = await result.current
        .register("jake@jake.taken", "jake", "fakefake")
        .catch((e) => e);
      expect(capturedError.response?.status).toEqual(422);
      expect(capturedError.response?.data).toEqual({
        errors: {
          email: ["has already been taken"],
        },
      });

      expect(store.get(userAtom)).toEqual(null);
      expect(getToken()).toEqual(null);
    });
  });
  test("when register email and username already exist", async () => {
    provider
      .given("both email and username already exist when registering")
      .uponReceiving("a request to register")
      .withRequest({
        method: "POST",
        path: "/user",
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          user: {
            email: "jake@jake.taken",
            username: "jake_exist",
            password: "fakefake",
          },
        },
      })
      .willRespondWith({
        status: 422,
        body: {
          errors: {
            email: ["has already been taken"],
            username: ["has already been taken"],
          },
        },
      });
    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController(), { wrapper });

      const capturedError: AxiosError = await result.current
        .register("jake@jake.taken", "jake_exist", "fakefake")
        .catch((e) => e);
      expect(capturedError.response?.status).toEqual(422);
      expect(capturedError.response?.data).toEqual({
        errors: {
          email: ["has already been taken"],
          username: ["has already been taken"],
        },
      });

      expect(store.get(userAtom)).toEqual(null);
      expect(getToken()).toEqual(null);
    });
  });
});
