import request from "../utils/request";
import { atom, useAtom } from "jotai";
import { clearToken, persistToken } from "../utils/token";
import { omit } from "lodash";
import { createContext } from "react";

export interface User {
  email: string;
  username: string;
  bio?: string;
  image?: string;
}

export interface UserController {
  loadCurrentUser(): Promise<void>;
  login(email: string, password: string): Promise<void>;
  register(email: string, username: string, password: string): Promise<void>;
  logout(): void;
}

export interface LoginUserResponse extends User {
  token: string;
}

export const userAtom = atom<User | null>(null);

export function useUserController(): UserController {
  const [, setUser] = useAtom(userAtom);
  return {
    async login(email: string, password: string) {
      const user = await login(email, password);
      setUser(user);
    },
    async register(email: string, username: string, password: string) {
      const user = await register(email, username, password);
      setUser(user);
    },
    async loadCurrentUser() {
      const user = await getCurrentUser();
      setUser(user);
    },
    logout() {
      clearToken();
      setUser(null);
    },
  };
}

export const UserControllerContext = createContext<UserController | null>(null);

async function login(email: string, password: string) {
  const res = await request().post<{ user: LoginUserResponse }>("/user/login", {
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
  const res = await request().post<{ user: LoginUserResponse }>("/user", {
    user: { email, username, password },
  });
  const user = res.data.user;
  persistToken(user.token);
  return omit(user, ["token"]);
}

async function getCurrentUser() {
  const res = await request().get<{ user: LoginUserResponse }>("/user");
  return res.data.user;
}
