import { RegisterForm } from "../components/RegisterForm";

export const Register = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-6 rounded shadow-md">
        <h2 className="text-2xl font-bold mb-4">Register</h2>
        <RegisterForm />
      </div>
    </div>
  );
};
