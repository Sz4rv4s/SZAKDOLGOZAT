import { create } from "zustand/react";
import { login, logout, refreshToken, register } from "../api/authApi.ts";
import { AuthState } from "../types/types";
import { useNotificationStore } from "./notificationStore.ts";

const setAuthData = (
  set: (state: Partial<AuthState>) => void,
  data: {
    accessToken: string;
    refreshToken: string;
    user: string;
    userId: number;
  },
) => {
  localStorage.setItem("accessToken", data.accessToken);
  localStorage.setItem("refreshToken", data.refreshToken);
  localStorage.setItem("user", data.user);
  localStorage.setItem("userId", data.userId.toString());
  set({
    accessToken: data.accessToken,
    refreshToken: data.refreshToken,
    user: data.user,
    userId: data.userId.toString(),
    isAuthenticated: true,
  });
};

const clearAuthData = (set: (state: Partial<AuthState>) => void) => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  localStorage.removeItem("user");
  localStorage.removeItem("userId");
  set({
    accessToken: null,
    refreshToken: null,
    user: null,
    userId: null,
    isAuthenticated: false,
  });
};

export const useAuthStore = create<AuthState>((set) => ({
  accessToken: localStorage.getItem("accessToken"),
  refreshToken: localStorage.getItem("refreshToken"),
  user: localStorage.getItem("user"),
  userId: localStorage.getItem("userId"),
  isAuthenticated: !!localStorage.getItem("accessToken"),
  login: async (username: string, password: string) => {
    const result = await login({ username, password });
    console.table(result);
    if (result.success && result.data) {
      setAuthData(set, result.data);
    } else {
      useNotificationStore
        .getState()
        .showNotification(result.error || "Login failed", "error");
    }
  },
  register: async (email: string, username: string, password: string) => {
    const result = await register({ email, username, password });
    if (result.success && result.data) {
      setAuthData(set, result.data);
    } else {
      useNotificationStore
        .getState()
        .showNotification(result.error || "Registration failed", "error");
    }
  },
  logout: async () => {
    const currentAccessToken = localStorage.getItem("accessToken");
    const currentRefreshToken = localStorage.getItem("refreshToken");
    if (currentAccessToken && currentRefreshToken) {
      const result = await logout(currentAccessToken, currentRefreshToken);
      if (!result.success) {
        useNotificationStore
          .getState()
          .showNotification(result.error || "Logout failed", "error");
      }
    }
    clearAuthData(set);
  },
  refresh: async () => {
    const currentAccessToken = localStorage.getItem("accessToken");
    const currentRefreshToken = localStorage.getItem("refreshToken");
    if (!currentRefreshToken || !currentAccessToken) {
      useNotificationStore
        .getState()
        .showNotification("No tokens available", "error");
      set({ accessToken: null, refreshToken: null, isAuthenticated: false });
      return { success: false, data: null, error: "No tokens available" };
    }
    const result = await refreshToken(currentAccessToken, currentRefreshToken);
    if (result.success) {
      localStorage.setItem("accessToken", result.data.accessToken);
      localStorage.setItem("refreshToken", result.data.refreshToken);
      set({
        accessToken: result.data.accessToken,
        refreshToken: result.data.refreshToken,
        isAuthenticated: true,
      });
      return { success: true, data: result.data, error: null };
    } else {
      useNotificationStore
        .getState()
        .showNotification(result.error || "Refresh failed", "error");
      set({
        accessToken: null,
        refreshToken: null,
        user: null,
        userId: null,
        isAuthenticated: false,
      });
      return {
        success: false,
        data: null,
        error: result.error || "Refresh failed",
      };
    }
  },
}));
