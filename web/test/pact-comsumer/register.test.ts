import { getDefaultStore } from "jotai";
import { describe, beforeEach, test, expect } from "vitest";
import { useUserController, userAtom } from "../../src/model/user";
import { provider } from "./pact.utils";
import { configEnv } from "../../src/utils/env";
import { initAxiosInstance } from "../../src/utils/request";
import { renderHook } from "@testing-library/react";
import { getToken } from "../../src/utils/token";
import {AxiosError} from "axios";

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
            "username": ["has already been taken"],
          },
        },
      });
    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController());

      const capturedError: AxiosError = await result.current
        .register("jake@jake.jake", "jake_exist", "fakefake")
        .catch((e) => e);
      expect(capturedError.response?.status).toEqual(422);
      expect(capturedError.response?.data).toEqual({
        errors: {
          "username": ["has already been taken"],
        },
      });

      expect(getDefaultStore().get(userAtom)).toEqual(null);
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
            "email": ["has already been taken"],
          },
        },
      });
    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController());

      const capturedError: AxiosError = await result.current
        .register("jake@jake.taken", "jake", "fakefake")
        .catch((e) => e);
      expect(capturedError.response?.status).toEqual(422);
      expect(capturedError.response?.data).toEqual({
        errors: {
          "email": ["has already been taken"],
        },
      });

      expect(getDefaultStore().get(userAtom)).toEqual(null);
      expect(getToken()).toEqual(null);
    });
  });
});
