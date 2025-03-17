import * as Yup from "yup";

export const loginSchema = Yup.object({
  username: Yup.string()
    .min(3, "Username must be at least 3 characters")
    .max(20, "Username must be 20 characters or less")
    .matches(
      /^[a-zA-Z0-9_]+$/,
      "Username can only contain letters, numbers, and underscores",
    )
    .required("Username is required"),
  password: Yup.string()
    .min(6, "Password must be at least 6 characters")
    .required("Password is required"),
});

export const registerSchema = Yup.object({
  email: Yup.string()
    .email("Invalid email address")
    .required("Email is required"),
  username: Yup.string()
    .min(3, "Username must be at least 3 characters")
    .max(20, "Username must be 20 characters or less")
    .matches(
      /^[a-zA-Z0-9_]+$/,
      "Username can only contain letters, numbers, and underscores",
    )
    .required("Username is required"),
  password: Yup.string()
    .min(6, "Password must be at least 6 characters")
    .required("Password is required"),
});
