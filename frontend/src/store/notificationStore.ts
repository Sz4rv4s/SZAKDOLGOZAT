import { create } from "zustand";
import { NotificationState } from "../types/types";

export const useNotificationStore = create<NotificationState>((set) => ({
  message: null,
  type: null,
  showNotification: (message, type) => set({ message, type }),
  clearNotification: () => set({ message: null, type: null }),
}));
