import { FC, useEffect, useState } from "react";
import { useAuthStore } from "../store/authStore";
import { LeaderboardEntry, MatchScoreBetDTO } from "../types/types";
import {
  getMonthlyLeaderboard,
  getUserBets,
  getWeeklyLeaderboard,
} from "../api/footballApi.ts";

const formatDisplayDateTime = (utcDate: string): string => {
  return new Intl.DateTimeFormat("hu-HU", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  })
    .format(new Date(utcDate))
    .replace(/(\d{4})\.(\d{2})\.(\d{2})\.\s/, "$1.$2.$3. ");
};

export const Profile: FC = () => {
  const { userId, isAuthenticated } = useAuthStore();
  const [bets, setBets] = useState<MatchScoreBetDTO[]>([]);
  const [weeklyPoints, setWeeklyPoints] = useState<number>(0);
  const [monthlyPoints, setMonthlyPoints] = useState<number>(0);
  const [isLoadingBets, setIsLoadingBets] = useState(true);
  const [isLoadingPoints, setIsLoadingPoints] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isAuthenticated || !userId) {
      setError("Please log in to view your profile");
      setIsLoadingBets(false);
      setIsLoadingPoints(false);
      return;
    }

    const fetchProfileData = async () => {
      try {
        const numericUserId = Number(userId);

        setIsLoadingBets(true);
        const betsResult = await getUserBets(numericUserId);
        if (betsResult.success && betsResult.data?.matchScoreBet) {
          setBets(betsResult.data.matchScoreBet);
        } else {
          setError(betsResult.error || "Failed to load your bets");
        }
        setIsLoadingBets(false);

        setIsLoadingPoints(true);
        const [weeklyResult, monthlyResult] = await Promise.all([
          getWeeklyLeaderboard(),
          getMonthlyLeaderboard(),
        ]);

        if (weeklyResult.success && weeklyResult.data) {
          const userEntry = weeklyResult.data.find(
            (entry: LeaderboardEntry) => entry.userId === numericUserId,
          );
          setWeeklyPoints(userEntry ? userEntry.totalPoints : 0);
        } else {
          setError(
            (prev) =>
              prev || weeklyResult.error || "Failed to load weekly points",
          );
        }

        if (monthlyResult.success && monthlyResult.data) {
          const userEntry = monthlyResult.data.find(
            (entry: LeaderboardEntry) => entry.userId === numericUserId,
          );
          setMonthlyPoints(userEntry ? userEntry.totalPoints : 0);
        } else {
          setError(
            (prev) =>
              prev || monthlyResult.error || "Failed to load monthly points",
          );
        }
        setIsLoadingPoints(false);
      } catch (error) {
        console.error(error);
        setError("An unexpected error occurred while fetching profile data");
        setIsLoadingBets(false);
        setIsLoadingPoints(false);
      }
    };

    fetchProfileData().then(() => {});
  }, [userId, isAuthenticated]);

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Your Profile</h1>

      {error && (
        <div className="mb-4 p-4 bg-red-100 text-red-700 rounded-lg">
          {error}
        </div>
      )}

      <div className="mb-8">
        <h2 className="text-2xl font-semibold mb-4">Your Points</h2>
        {isLoadingPoints ? (
          <div className="flex justify-center items-center h-32">
            <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="bg-white shadow-md rounded-lg p-4">
              <h3 className="text-lg font-medium text-gray-700">
                Weekly Points
              </h3>
              <p className="text-2xl font-bold text-blue-600">{weeklyPoints}</p>
            </div>
            <div className="bg-white shadow-md rounded-lg p-4">
              <h3 className="text-lg font-medium text-gray-700">
                Monthly Points
              </h3>
              <p className="text-2xl font-bold text-blue-600">
                {monthlyPoints}
              </p>
            </div>
          </div>
        )}
      </div>

      <div className="mb-8">
        <h2 className="text-2xl font-semibold mb-4">Your Bets</h2>
        {isLoadingBets ? (
          <div className="flex justify-center items-center h-32">
            <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
          </div>
        ) : bets.length > 0 ? (
          <div className="bg-white shadow-md rounded-lg overflow-hidden">
            <table className="min-w-full">
              <thead className="bg-gray-200">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Match
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Date
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Your Bet
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actual Score
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Points
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200">
                {bets.map((bet, index) => (
                  <tr
                    key={bet.id}
                    className={index % 2 === 0 ? "bg-gray-50" : "bg-white"}
                  >
                    <td className="px-6 py-4 whitespace-nowrap">
                      {bet.homeTeamShortName} vs {bet.awayTeamShortName}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {formatDisplayDateTime(bet.dateOfMatch)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {bet.homeScoreBet} - {bet.awayScoreBet}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {bet.homeScore !== null && bet.awayScore !== null
                        ? `${bet.homeScore} - ${bet.awayScore}`
                        : "Not yet played"}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {bet.points !== null ? bet.points : "Pending"}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {bet.status}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-gray-500">You haven't placed any bets yet</p>
        )}
      </div>
    </div>
  );
};

export default Profile;
