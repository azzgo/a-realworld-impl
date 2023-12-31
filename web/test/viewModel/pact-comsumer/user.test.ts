import { createStore } from "jotai";
import { describe, beforeEach, test, expect } from "vitest";
import {
  UpdatedUserInfo,
  useUserController,
  userAtom,
} from "../../../src/model/user";
import { jwtToken, provider } from "./pact.utils";
import { configEnv } from "../../../src/utils/env";
import { initAxiosInstance } from "../../../src/utils/request";
import { renderHook } from "@testing-library/react";
import { getToken, persistToken } from "../../../src/utils/token";
import { AxiosError } from "axios";
import { JotaiStore } from "../../../src/type";
import { MockHeadlessStoreWrapper } from "../../views/utils.utils";

describe("consumer test for register", () => {
  let store: JotaiStore;
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
        path: "/users",
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
            token: jwtToken,
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
      expect(getToken()).toEqual(jwtToken);
    });
  });

  test("when register username already exist", async () => {
    provider
      .given("username already exist when registering")
      .uponReceiving("a request to register")
      .withRequest({
        method: "POST",
        path: "/users",
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
        path: "/users",
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
        path: "/users",
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

describe("comsumer test for login", () => {
  let store: JotaiStore;
  let wrapper: any;
  beforeEach(() => {
    localStorage.clear();
    store = createStore();
    wrapper = MockHeadlessStoreWrapper(store);
  });
  test("when login succuess should update store and token", async () => {
    provider
      .given("user exists")
      .uponReceiving("a request to login")
      .withRequest({
        method: "POST",
        path: "/users/login",
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
            token: jwtToken,
            username: "jake",
            bio: "I work at statefarm",
            image: "http://image.url",
          },
        },
      });

    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController(), { wrapper });
      await result.current.login("jake@jake.jake", "jakejake");
      expect(store.get(userAtom)).toEqual({
        email: "jake@jake.jake",
        username: "jake",
        bio: "I work at statefarm",
        image: "http://image.url",
      });
      expect(getToken()).toEqual(jwtToken);
    });
  });

  test("when login failed should not update store and token and throw error", async () => {
    provider
      .given("user not or password invalid")
      .uponReceiving("a request to login")
      .withRequest({
        method: "POST",
        path: "/users/login",
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
      const { result } = renderHook(() => useUserController(), { wrapper });

      const capturedError: AxiosError = await result.current
        .login("fake@jake.jake", "fakefake")
        .catch((e) => e);
      expect(capturedError.response?.status).toEqual(403);
      expect(capturedError.response?.data).toEqual({
        errors: {
          "email or password": ["is invalid"],
        },
      });

      expect(store.get(userAtom)).toEqual(null);
      expect(getToken()).toEqual(null);
    });
  });
});

describe("comsumer test for loadCurrentUser", () => {
  let store: JotaiStore;
  let wrapper: any;
  beforeEach(() => {
    localStorage.clear();
    store = createStore();
    wrapper = MockHeadlessStoreWrapper(store);
  });
  test("when loadCurrentUser succuess should update store and token", async () => {
    persistToken(jwtToken);
    provider
      .given("user exist and get user info")
      .uponReceiving("a request to get user")
      .withRequest({
        method: "GET",
        path: "/user",
        headers: {
          Authorization: `Token ${jwtToken}`,
        },
      })
      .willRespondWith({
        status: 200,
        headers: { "Content-Type": "application/json" },
        body: {
          user: {
            email: "jake@jake.jake",
            bio: "I work at statefarm",
            token: jwtToken,
            image: "http://image.url",
            username: "jake",
          },
        },
      });

    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController(), { wrapper });
      await result.current.loadCurrentUser();
      expect(store.get(userAtom)).toEqual({
        email: "jake@jake.jake",
        bio: "I work at statefarm",
        image: "http://image.url",
        username: "jake",
      });
      expect(getToken()).toEqual(jwtToken);
    });
  });
});

describe("comsumer test for update", () => {
  let store: JotaiStore;
  let wrapper: any;
  beforeEach(() => {
    localStorage.clear();
    store = createStore();
    wrapper = MockHeadlessStoreWrapper(store);
  });
  test("when updating user info", async () => {
    persistToken(jwtToken);
    const updatedUserInfo: UpdatedUserInfo = {
      username: "jake John",
      email: "jake-john@jake.jake",
      bio: "I work at statefarm",
      image: "http://image.url",
      password: "jakejakejake",
    };
    provider
      .given("user exists and update user info")
      .uponReceiving("a request to update user info")
      .withRequest({
        method: "PUT",
        path: "/user",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Token ${jwtToken}`,
        },
        body: { user: updatedUserInfo },
      })
      .willRespondWith({
        status: 200,
        headers: { "Content-Type": "application/json" },
        body: {
          user: {
            username: "jake John",
            email: "jake-john@jake.jake",
            token: jwtToken,
            bio: "I work at statefarm",
            image: "http://image.url",
          },
        },
      });

    return provider.executeTest(async (mockServer) => {
      configEnv({ BASE_URL: mockServer.url });
      initAxiosInstance();
      const { result } = renderHook(() => useUserController(), { wrapper });

      await result.current.update(updatedUserInfo);
      expect(store.get(userAtom)).toEqual({
        username: "jake John",
        email: "jake-john@jake.jake",
        bio: "I work at statefarm",
        image: "http://image.url",
      });
      expect(getToken()).toEqual(jwtToken);
    });
  });
});
