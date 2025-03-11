declare interface LoginCredentials {
  username: string;
  password: string;
}

declare interface RegisterCredentials extends LoginCredentials {
  email: string;
}

declare interface AuthTokens {
  accessToken: string;
  refreshToken: string;
}

declare interface UserProfile {
  username: string;
  email: string;
}

declare interface AuthState {
  user: UserProfile | null;
  tokens: AuthTokens | null;
  login: (userData: UserProfile, tokens: AuthTokens) => void;
  logout: () => void;
  isAuthenticated: () => boolean;
}
