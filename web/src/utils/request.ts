import axios, { AxiosInstance } from "axios";
import { getEnvByKey } from "./env";
import { clearToken, getToken } from "./token";
import { redirect } from "react-router";
import {JotaiStore} from "../type";
import {userAtom} from "../model/user";

let request: AxiosInstance | null = null;

export const ErrorCodeKey = {
  emailOrPasssword: "email or passsword",
};

export function initAxiosInstance(store: JotaiStore | null = null) {
  request = axios.create({
    baseURL: getEnvByKey("BASE_URL"),
    headers: {
      "Content-Type": "application/json",
    },
    timeout: 60000,
  });

  // Add request interceptor
  request.interceptors.request.use(
    (config: any) => {
      // Do something before request is sent
      if (getToken()) {
        config.headers.Authorization = `Token ${getToken()}`;
      }
      return config;
    },
    (error: any) => {
      // Do something with request error
      return Promise.reject(error);
    }
  );

  // Add response interceptor
  request.interceptors.response.use(
    (response: any) => {
      // Do something with response data
      return response;
    },
    (error: any) => {
      if (error.response && error.response.status === 401) {
        clearToken();
        store?.set(userAtom, null);
        redirect("/login");
      }
      return Promise.reject(error);
    }
  );
}

export default function requestInstance() {
  if (!request) {
    initAxiosInstance();
  }
  return request!;
}
