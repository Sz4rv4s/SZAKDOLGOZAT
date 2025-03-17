import { Formik, Form, Field, ErrorMessage } from "formik";
import { useAuthStore } from "../store/authStore";
import { registerSchema } from "../validation/authSchemas";
import { useNavigate } from "react-router-dom";

export const RegisterForm = () => {
  const register = useAuthStore((state) => state.register);
  const navigate = useNavigate();

  return (
    <Formik
      initialValues={{ email: "", username: "", password: "" }}
      validationSchema={registerSchema}
      onSubmit={async (values, { setSubmitting, setErrors }) => {
        try {
          await register(values.email, values.username, values.password);
          navigate("/");
        } catch (err) {
          setErrors({ username: "Registration failed." });
          console.error(err);
        } finally {
          setSubmitting(false);
        }
      }}
    >
      {({ isSubmitting }) => (
        <Form className="space-y-4 max-w-md mx-auto p-4">
          <div>
            <label htmlFor="email" className="block text-sm font-medium">
              Email
            </label>
            <Field
              type="email"
              name="email"
              className="w-full p-2 border rounded"
              disabled={isSubmitting}
            />
            <ErrorMessage
              name="email"
              component="p"
              className="text-red-500 text-sm"
            />
          </div>
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
            className="w-full bg-green-500 text-white p-2 rounded disabled:bg-green-300"
          >
            {isSubmitting ? "Registering..." : "Register"}
          </button>
        </Form>
      )}
    </Formik>
  );
};
