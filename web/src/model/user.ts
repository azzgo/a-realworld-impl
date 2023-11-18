import request from "../utils/request";
import { atom, useAtomValue, useSetAtom } from "jotai";
import { clearToken, persistToken } from "../utils/token";
import { omit } from "lodash";
import { createContext } from "react";

export interface User {
  email: string;
  username: string;
  bio?: string;
  image?: string;
}

export interface UpdatedUserInfo extends Partial<User> {
  password?: string;
}

export interface UserController {
  loadCurrentUser(): Promise<void>;
  login(email: string, password: string): Promise<void>;
  register(email: string, username: string, password: string): Promise<void>;
  update(toUpdateUserInfo: UpdatedUserInfo): Promise<void>;
  logout(): void;
}

export interface AuthenticatedUserResponse extends User {
  token: string;
}

export const userAtom = atom<User | null>(null);

export function useIsLogin(): boolean {
  const user = useAtomValue(userAtom);
  return user != null;
}

export function useUserController(): UserController {
  const updateUser = useSetAtom(userAtom);
  return {
    async login(email: string, password: string) {
      const user = await login(email, password);
      updateUser(user);
    },
    async register(email: string, username: string, password: string) {
      const user = await register(email, username, password);
      updateUser(user);
    },
    async loadCurrentUser() {
      const user = await getCurrentUser();
      updateUser(user);
    },
    async update(toUpdateUserInfo: Partial<User>) {
      const user = await updateUserInfo(toUpdateUserInfo);
      updateUser(user);
    },
    logout() {
      clearToken();
      updateUser(null);
    },
  };
}

export const UserControllerContext = createContext<UserController | null>(null);

async function login(email: string, password: string) {
  const res = await request().post<{ user: AuthenticatedUserResponse }>("/users/login", {
    user: { email, password },
  });
  const user = res.data.user;
  persistToken(user.token);
  return omit(user, ["token"]);
}

async function register(
  email: string,
  username: string,
  password: string
) {
  const res = await request().post<{ user: AuthenticatedUserResponse }>("/users", {
    user: { email, username, password },
  });
  const user = res.data.user;
  persistToken(user.token);
  return omit(user, ["token"]);
}

async function getCurrentUser() {
  const res = await request().get<{ user: AuthenticatedUserResponse }>("/user");
  const user = res.data.user;
  persistToken(user.token);
  return omit(user, ["token"]);
}

async function updateUserInfo(toUpdateUserInfo: UpdatedUserInfo) {
  const res = await request().put<{ user: AuthenticatedUserResponse }>("/user", {
    user: toUpdateUserInfo,
  });
  const user = res.data.user;
  persistToken(user.token);
  return omit(user, ["token"]);
}
