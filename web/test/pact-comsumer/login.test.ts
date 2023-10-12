import { describe, expect, test } from "vitest";
import { renderHook } from "@testing-library/react";
import { provider } from "./pact.utils";
import { configEnv } from "../../src/utils/env";
import { useUserController, userAtom } from "../../src/model/user";
import { initAxiosInstance } from "../../src/utils/request";
import { getDefaultStore } from "jotai";
import { getToken } from "../../src/utils/token";

describe("comsumer test for login", () => {
  test("when login succuess should update store and token", async () => {
    provider
      .given("user exists")
      .uponReceiving("a request to login")
      .withRequest({
        method: "POST",
        path: "/login",
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
      const {  result } = renderHook(() => useUserController());
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
});
