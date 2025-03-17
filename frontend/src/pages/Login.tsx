import { LoginForm } from "../components/LoginForm";

export const Login = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-6 rounded shadow-md">
        <h2 className="text-2xl font-bold mb-4">Login</h2>
        <LoginForm />
      </div>
    </div>
  );
};
