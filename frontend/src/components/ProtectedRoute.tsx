import { Navigate } from "react-router-dom";
import { useAuthStore } from "../store/authStore";
import { JSX } from "react";

export const ProtectedRoute = ({ children }: { children: JSX.Element }) => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  return children;
};
