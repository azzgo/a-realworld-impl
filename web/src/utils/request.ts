import axios, { AxiosInstance } from "axios";
import { getEnvByKey } from "./env";
import { getToken } from "./token";

let request: AxiosInstance | null = null;

export const ErrorCodeKey = {
  emailOrPasssword: "email or passsword",
};

export function initAxiosInstance() {
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
      // Do something with response error
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
