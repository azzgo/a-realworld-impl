import { render } from "@testing-library/react";
import { beforeEach, expect, test, vi } from "vitest";
import { MockAppWrapper } from "./utils.utils";
import { UserController, UserControllerContext } from "../../src/model/user";
import React from "react";
import App from "../../src/App";
import { persistToken } from "../../src/utils/token";

let mockUserController: UserController;

beforeEach(() => {
  mockUserController = {
    loadCurrentUser: vi.fn(),
  } as any;
});

test.skip("should get current user when render App", () => {
  (mockUserController.loadCurrentUser as any).mockResolvedValue(undefined);
  persistToken("token");
  renderApp();

  expect(mockUserController.loadCurrentUser).toHaveBeenCalledOnce();
});

function renderApp() {
  const Wrapper = MockAppWrapper();

  return render(
    <Wrapper>
      <UserControllerContext.Provider value={mockUserController}>
        <App />
      </UserControllerContext.Provider>
    </Wrapper>
  );
}
