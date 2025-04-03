import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";
import { Home } from "./pages/Home";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { Header } from "./components/Header.tsx";
import { Notification } from "./components/Notification.tsx";
import Leaderboards from "./pages/Leaderboards.tsx";
import Profile from "./components/Profile.tsx";
import Admin from "./components/Admin.tsx";
import { useAuthStore } from "./store/authStore.ts";

function App() {
  const { roles } = useAuthStore();

  return (
    <BrowserRouter>
      <div className="flex flex-col min-h-screen">
        <Header />
        <Notification />
        <main className="flex-grow pt-16">
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route
              path="/"
              element={
                <ProtectedRoute>
                  <Home />
                </ProtectedRoute>
              }
            />
            {roles === "ADMIN" && (
              <Route
                path="/admin"
                element={
                  <ProtectedRoute>
                    <Admin />
                  </ProtectedRoute>
                }
              />
            )}
            <Route
              path="/leaderboards"
              element={
                <ProtectedRoute>
                  <Leaderboards />
                </ProtectedRoute>
              }
            />
            <Route
              path="/profile"
              element={
                <ProtectedRoute>
                  <Profile />
                </ProtectedRoute>
              }
            />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
