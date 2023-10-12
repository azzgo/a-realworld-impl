import request from "../utils/request";
import { atom, getDefaultStore, useAtom } from "jotai";
import { persistToken } from "../utils/token";
import { omit } from "lodash";

export interface User {
  email: string;
  username: string;
  bio?: string;
  image?: string;
}

export interface LoginUserResponse extends User {
  token: string;
}

export const userAtom = atom<User | null>(null);

export function useUserController() {
  const [_, setUser] = useAtom(userAtom);
  return {
    async login(email: string, password: string) {
      const user = await login(email, password);
      setUser(user);
    }
  }
}

export async function login(email: string, password: string) {
  const res = await request().post<{ user: LoginUserResponse }>("/login", {
    user: { email, password },
  });
  const user = res.data.user;
  persistToken(user.token);
  return omit(user, ['token']);
}
