import { FC, useEffect, useState } from "react";
import {
  getWeeklyLeaderboard,
  getMonthlyLeaderboard,
  getUser,
} from "../api/footballApi.ts";
import { LeaderboardUser } from "../types/types";

export const Leaderboards: FC = () => {
  const [weeklyLeaderboard, setWeeklyLeaderboard] = useState<LeaderboardUser[]>(
    [],
  );
  const [monthlyLeaderboard, setMonthlyLeaderboard] = useState<
    LeaderboardUser[]
  >([]);
  const [isLoadingWeekly, setIsLoadingWeekly] = useState(true);
  const [isLoadingMonthly, setIsLoadingMonthly] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchLeaderboards = async () => {
      try {
        setIsLoadingWeekly(true);
        const weeklyResult = await getWeeklyLeaderboard();
        if (weeklyResult.success && weeklyResult.data) {
          const weeklyWithUsers = await Promise.all(
            weeklyResult.data.map(async (entry) => {
              const userResult = await getUser(entry.userId);
              return {
                ...entry,
                username:
                  userResult.success && userResult.data
                    ? userResult.data.username
                    : `User ${entry.userId}`,
              };
            }),
          );
          setWeeklyLeaderboard(weeklyWithUsers);
        } else {
          setError(weeklyResult.error || "Failed to load weekly leaderboard");
        }
        setIsLoadingWeekly(false);

        setIsLoadingMonthly(true);
        const monthlyResult = await getMonthlyLeaderboard();
        if (monthlyResult.success && monthlyResult.data) {
          const monthlyWithUsers = await Promise.all(
            monthlyResult.data.map(async (entry) => {
              const userResult = await getUser(entry.userId);
              return {
                ...entry,
                username:
                  userResult.success && userResult.data
                    ? userResult.data.username
                    : `User ${entry.userId}`,
              };
            }),
          );
          setMonthlyLeaderboard(monthlyWithUsers);
        } else {
          setError(monthlyResult.error || "Failed to load monthly leaderboard");
        }
        setIsLoadingMonthly(false);
      } catch (error) {
        console.error(error);
        setError("An unexpected error occurred while fetching leaderboards");
        setIsLoadingWeekly(false);
        setIsLoadingMonthly(false);
      }
    };

    fetchLeaderboards().then(() => {});
  }, []);

  const renderLeaderboard = (
    title: string,
    data: LeaderboardUser[],
    isLoading: boolean,
  ) => (
    <div className="mb-8">
      <h2 className="text-2xl font-semibold mb-4">{title}</h2>
      {isLoading ? (
        <div className="flex justify-center items-center h-32">
          <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      ) : data.length > 0 ? (
        <div className="bg-white shadow-md rounded-lg overflow-hidden">
          <table className="min-w-full">
            <thead className="bg-gray-200">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Rank
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Username
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Points
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {data.map((entry, index) => (
                <tr
                  key={entry.userId}
                  className={index % 2 === 0 ? "bg-gray-50" : "bg-white"}
                >
                  <td className="px-6 py-4 whitespace-nowrap">{index + 1}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {entry.username}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {entry.totalPoints}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p className="text-gray-500">No leaderboard data available</p>
      )}
    </div>
  );

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Leaderboards</h1>

      {error && (
        <div className="mb-4 p-4 bg-red-100 text-red-700 rounded-lg">
          {error}
        </div>
      )}

      {renderLeaderboard(
        "Weekly Leaderboard",
        weeklyLeaderboard,
        isLoadingWeekly,
      )}
      {renderLeaderboard(
        "Monthly Leaderboard",
        monthlyLeaderboard,
        isLoadingMonthly,
      )}
    </div>
  );
};

export default Leaderboards;
