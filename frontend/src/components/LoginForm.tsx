import { Formik, Form, Field, ErrorMessage } from "formik";
import { useAuthStore } from "../store/authStore";
import { loginSchema } from "../validation/authSchemas";
import { useNavigate } from "react-router-dom";

export const LoginForm = () => {
  const login = useAuthStore((state) => state.login);
  const navigate = useNavigate();

  return (
    <Formik
      initialValues={{ username: "", password: "" }}
      validationSchema={loginSchema}
      onSubmit={async (values, { setSubmitting, setErrors }) => {
        try {
          await login(values.username, values.password);
          navigate("/");
        } catch (err) {
          setErrors({ password: "Login failed. Check your credentials." });
          console.error(err);
        } finally {
          setSubmitting(false);
        }
      }}
    >
      {({ isSubmitting }) => (
        <Form className="space-y-4 max-w-md mx-auto p-4">
          <div>
            <label htmlFor="username" className="block text-sm font-medium">
              Username
            </label>
            <Field
              type="text"
              name="username"
              className="w-full p-2 border rounded"
              disabled={isSubmitting}
            />
            <ErrorMessage
              name="username"
              component="p"
              className="text-red-500 text-sm"
            />
          </div>
          <div>
            <label htmlFor="password" className="block text-sm font-medium">
              Password
            </label>
            <Field
              type="password"
              name="password"
              className="w-full p-2 border rounded"
              disabled={isSubmitting}
            />
            <ErrorMessage
              name="password"
              component="p"
              className="text-red-500 text-sm"
            />
          </div>
          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-blue-500 text-white p-2 rounded disabled:bg-blue-300"
          >
            {isSubmitting ? "Logging in..." : "Login"}
          </button>
        </Form>
      )}
    </Formik>
  );
};
