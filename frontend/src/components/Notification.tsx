import { useNotificationStore } from "../store/notificationStore";

export const Notification = () => {
  const { message, type, clearNotification } = useNotificationStore();

  if (!message || !type) return null;

  const bgColor =
    type === "error"
      ? "bg-red-500"
      : type === "success"
        ? "bg-green-500"
        : "bg-blue-500";

  return (
    <div
      className={`fixed top-20 right-10 p-4 rounded-lg text-white ${bgColor} shadow-lg`}
    >
      <p>{message}</p>
      <button onClick={clearNotification} className="ml-2 text-sm underline">
        Close
      </button>
    </div>
  );
};
