import axios, {
  AxiosError,
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
} from "axios";
import { useAuthStore } from "../store/authStore";
import { useNotificationStore } from "../store/notificationStore.ts";
import { ApiErrorResponse } from "../types/types";

const api = axios.create({
  baseURL: process.env.VITE_API_URL || "/api",
  headers: {
    "Content-Type": "application/json",
  },
});

let isRefreshing = false;
let failedQueue: {
  resolve: (value: string | null) => void;
  reject: (reason?: string | null) => void;
}[] = [];

const processQueue = (error: string | null, token: string | null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

const applyRequestInterceptor = (instance: AxiosInstance) => {
  instance.interceptors.request.use((config) => {
    const token = localStorage.getItem("accessToken");
    console.log("Interceptor - URL:", config.url, "Token:", token);
    if (
      token &&
      !["/auth/login", "/auth/register"].includes(config.url || "")
    ) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });
};

const applyResponseInterceptor = (instance: AxiosInstance) => {
  instance.interceptors.response.use(
    (response: AxiosResponse) => response,
    async (error: AxiosError<ApiErrorResponse>) => {
      const originalRequest = error.config as AxiosRequestConfig & {
        _retry?: boolean;
      };
      const notify = useNotificationStore.getState().showNotification;

      if (
        error.response?.status === 401 &&
        !originalRequest._retry &&
        !["/auth/login", "/auth/register"].includes(originalRequest.url || "")
      ) {
        if (isRefreshing) {
          return new Promise((resolve, reject) => {
            failedQueue.push({ resolve, reject });
          })
            .then((token) => {
              originalRequest.headers!.Authorization = `Bearer ${token}`;
              return instance(originalRequest);
            })
            .catch((err) => Promise.reject(err));
        }

        originalRequest._retry = true;
        isRefreshing = true;

        const refreshToken = localStorage.getItem("refreshToken");
        const accessToken = localStorage.getItem("accessToken");
        if (!refreshToken || !accessToken) {
          notify("Session expired. Please log in again.", "error");
          await useAuthStore.getState().logout();
          return Promise.reject("No tokens available");
        }

        try {
          const result = await useAuthStore.getState().refresh();
          if (!result.success) {
            console.error(result.error || "Refresh failed");
          }
          const newAccessToken = localStorage.getItem("accessToken");
          if (!newAccessToken) {
            console.error("No new access token");
          }

          originalRequest.headers!.Authorization = `Bearer ${newAccessToken}`;
          processQueue(null, newAccessToken);
          return instance(originalRequest);
        } catch (refreshError) {
          notify("Session expired. Please log in again.", "error");
          await useAuthStore.getState().logout();
          processQueue("Refresh failed", null);
          return Promise.reject(refreshError);
        } finally {
          isRefreshing = false;
        }
      }

      const errorMessage =
        error.response?.data?.message ||
        error.message ||
        "An unexpected error occurred";
      notify(errorMessage, "error");
      return Promise.reject(errorMessage);
    },
  );
};

applyRequestInterceptor(api);
applyResponseInterceptor(api);

export default api;

export const createApiClient = (basePath: string): AxiosInstance => {
  const instance = axios.create({
    baseURL: `${process.env.VITE_API_URL || "/api"}${basePath}`,
    headers: { "Content-Type": "application/json" },
  });
  applyRequestInterceptor(instance);
  applyResponseInterceptor(instance);
  return instance;
};
