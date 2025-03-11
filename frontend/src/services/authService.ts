import axios from "axios";
import { useAuthStore } from "../utils/useAuthStore.ts";

const API_BASE_URL =
  import.meta.env.API_BASE_URL || "http://localhost:8080/api";

export const authService = {
  async login(
    credentials: LoginCredentials,
  ): Promise<{ user: UserProfile; tokens: AuthTokens }> {
    try {
      const response = await axios.post<{
        user: UserProfile;
        tokens: AuthTokens;
      }>(`${API_BASE_URL}/auth/login`, credentials);
      return response.data;
    } catch (error) {
      console.log(error);
      throw error;
    }
  },

  async register(
    credentials: RegisterCredentials,
  ): Promise<{ user: UserProfile; tokens: AuthTokens }> {
    try {
      const response = await axios.post<{
        user: UserProfile;
        tokens: AuthTokens;
      }>(`${API_BASE_URL}/auth/register`, credentials);
      return response.data;
    } catch (error) {
      console.log(error);
      throw error;
    }
  },

  async refreshTokens(refreshToken: string): Promise<AuthTokens> {
    try {
      const response = await axios.post<AuthTokens>(
        `${API_BASE_URL}/auth/refresh`,
        { refreshToken },
      );
      return response.data;
    } catch (error) {
      this.logout();
      throw error;
    }
  },

  logout() {
    useAuthStore.getState().logout();
  },
};
