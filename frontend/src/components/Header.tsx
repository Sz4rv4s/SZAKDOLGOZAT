import { useAuthStore } from "../store/authStore";
import { useNavigate, Link } from "react-router-dom";
import SoccerBallIcon from "../assets/soccer-ball.svg";

export const Header = () => {
  const { isAuthenticated, logout, user, roles } = useAuthStore();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate("/login");
  };

  return (
    <header className="fixed top-0 left-0 w-full bg-gray-800 text-white p-4 flex justify-between items-center shadow-md z-10">
      <Link to="/" className="flex items-center space-x-2 text-xl font-bold">
        <img
          src={SoccerBallIcon}
          alt="GoalRush Icon"
          className="w-8 h-8 text-orange-500"
          style={{
            filter:
              "invert(47%) sepia(99%) saturate(1352%) hue-rotate(3deg) brightness(97%) contrast(101%)",
          }}
        />
        <span>GoalRush</span>
      </Link>
      <nav className="flex items-center space-x-4">
        {isAuthenticated ? (
          <>
            <Link
              to="/leaderboards"
              className="bg-blue-500 hover:bg-blue-600 px-3 py-1 rounded"
            >
              Leaderboards
            </Link>
            {roles === "ADMIN" && (
              <Link
                to="/admin"
                className="bg-purple-500 hover:bg-purple-600 px-3 py-1 rounded"
              >
                Admin
              </Link>
            )}
            <Link
              to="/profile"
              className="bg-blue-500 hover:bg-blue-600 px-3 py-1 rounded"
            >
              Profile
            </Link>
            <span className="text-md">Welcome, {user || "User"}</span>
            <button
              onClick={handleLogout}
              className="bg-red-500 hover:bg-red-600 px-3 py-1 rounded"
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <Link
              to="/login"
              className="bg-blue-500 hover:bg-blue-600 px-3 py-1 rounded"
            >
              Login
            </Link>
            <Link
              to="/register"
              className="bg-green-500 hover:bg-green-600 px-3 py-1 rounded"
            >
              Register
            </Link>
          </>
        )}
      </nav>
    </header>
  );
};

export default Header;
