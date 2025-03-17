import { createApiClient } from "./api";
import {
  ApiResponse,
  AuthTokens,
  LoginCredentials,
  RegisterCredentials,
  UserProfile,
} from "../types/types";

const authApi = createApiClient("/auth");

export const login = async (
  data: LoginCredentials,
): Promise<ApiResponse<UserProfile>> => {
  try {
    const response = await authApi.post("/login", data);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    return { success: false, data: null, error: error as string };
  }
};

export const register = async (
  data: RegisterCredentials,
): Promise<ApiResponse<UserProfile>> => {
  try {
    const response = await authApi.post("/register", data);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    return { success: false, data: null, error: error as string };
  }
};

export const logout = async (
  accessToken: string,
  refreshToken: string,
): Promise<ApiResponse<void>> => {
  try {
    await authApi.post(
      "/logout",
      { refreshToken },
      { headers: { Authorization: `Bearer ${accessToken}` } },
    );
    return { success: true, data: undefined, error: null };
  } catch (error) {
    return { success: false, data: null, error: error as string };
  }
};

export const refreshToken = async (
  accessToken: string,
  refreshToken: string,
): Promise<ApiResponse<AuthTokens>> => {
  try {
    const response = await authApi.post(
      "/refresh",
      { refreshToken },
      { headers: { Authorization: `Bearer ${accessToken}` } },
    );
    return { success: true, data: response.data, error: null };
  } catch (error) {
    return { success: false, data: null, error: error as string };
  }
};

export default authApi;
