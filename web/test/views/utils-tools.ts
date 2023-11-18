import { userAtom } from "../../src/model/user";
import {JotaiStore} from "../../src/type";

export function fakeLoginUser(store: JotaiStore) {
  store.set(userAtom, { username: "anyUser" } as any);
}
