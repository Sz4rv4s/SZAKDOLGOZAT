import { create } from "zustand/react";
import { persist } from "zustand/middleware/persist";

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      tokens: null,
      login: (userData, tokens) => set({ user: userData, tokens }),
      logout: () => set({ user: null, tokens: null }),
      isAuthenticated: () => {
        const { tokens } = get();
        return !!tokens?.accessToken;
      },
    }),
    {
      name: "auth-storage",
      partialize: (state) => ({
        user: state.user,
        tokens: state.tokens,
      }),
    },
  ),
);
