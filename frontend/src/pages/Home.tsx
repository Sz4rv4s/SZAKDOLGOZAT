import { FC, useEffect, useState } from "react";
import {
  cancelMatchScoreBet,
  getLeagues,
  getMatchScoreBet,
  getUpcomingMatchesForLeague,
  makeMatchScoreBet,
  updateMatchScoreBet,
} from "../api/footballApi.ts";
import { Competition, Match } from "../types/types";
import { useAuthStore } from "../store/authStore.ts";

export const Home: FC = () => {
  const [leagues, setLeagues] = useState<Competition[]>([]);
  const [matches, setMatches] = useState<Match[]>([]);
  const [selectedMatch, setSelectedMatch] = useState<Match | null>(null);
  const [homeScoreBet, setHomeScoreBet] = useState<number>(0);
  const [awayScoreBet, setAwayScoreBet] = useState<number>(0);
  const [existingBet, setExistingBet] = useState<{
    homeScoreBet: number;
    awayScoreBet: number;
  } | null>(null);
  const { userId } = useAuthStore();

  useEffect(() => {
    const fetchData = async () => {
      const leaguesResult = await getLeagues();
      if (leaguesResult.success && leaguesResult.data?.competitions) {
        setLeagues(leaguesResult.data.competitions);
        if (leaguesResult.data.competitions.length > 0) {
          const matchesResult = await getUpcomingMatchesForLeague(
            leaguesResult.data.competitions[0].id,
          );
          if (matchesResult.success && matchesResult.data?.match) {
            setMatches(matchesResult.data.match);
          }
        }
      }
    };
    fetchData().then(() => {});
  }, []);

  const formatDateTime = (utcDate: string): string => {
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

  const nowDateTime = (): string => {
    return new Intl.DateTimeFormat("hu-HU", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    })
      .format(new Date())
      .replace(/(\d{4})\.(\d{2})\.(\d{2})\.\s/, "$1.$2.$3. ");
  };

  const openBetModal = async (match: Match) => {
    setSelectedMatch(match);
    setHomeScoreBet(0);
    setAwayScoreBet(0);
    const betResult = await getMatchScoreBet(Number(userId), match.id);
    if (betResult.success && betResult.data?.bet) {
      setExistingBet(betResult.data.bet);
      setHomeScoreBet(betResult.data.bet.homeScoreBet);
      setAwayScoreBet(betResult.data.bet.awayScoreBet);
    } else {
      setExistingBet(null);
    }
  };

  const closeBetModal = () => {
    setSelectedMatch(null);
    setExistingBet(null);
  };

  const handleBetSubmit = async () => {
    if (!selectedMatch || !userId) return;
    const bet = {
      id: 0,
      userId: Number(userId),
      matchId: selectedMatch.id,
      homeScoreBet,
      awayScoreBet,
      status: "IN_PLAY",
      date: nowDateTime(),
    };
    const result = await (existingBet
      ? updateMatchScoreBet(Number(userId), selectedMatch.id, {
          homeScoreBet,
          awayScoreBet,
        })
      : makeMatchScoreBet(bet));
    if (result.success) {
      alert(result.data?.message || "Bet processed successfully");
      closeBetModal();
    } else {
      alert(result.error || "Failed to process bet");
    }
  };

  const handleCancelBet = async () => {
    if (!selectedMatch) return;
    const result = await cancelMatchScoreBet(Number(userId), selectedMatch.id);
    if (result.success) {
      alert(result.data?.message || "Bet cancelled successfully");
      closeBetModal();
    } else {
      alert(result.error || "Failed to cancel bet");
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Upcoming matches</h1>
      <ul className="space-y-2">
        {matches.map((match) => (
          <li
            key={match.id}
            className="flex items-center justify-between bg-gray-100 p-2 rounded"
          >
            <span>
              {match.homeTeamShortName} vs {match.awayTeamShortName} -{" "}
              {formatDateTime(match.utcDate)}
            </span>
            <button
              onClick={() => openBetModal(match)}
              className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
            >
              Make bet
            </button>
          </li>
        ))}
      </ul>

      {selectedMatch && (
        <>
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
            <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
              <h2 className="text-xl font-semibold mb-4">
                Make bet: {selectedMatch.homeTeamShortName} vs{" "}
                {selectedMatch.awayTeamShortName}
              </h2>
              <p className="mb-2">
                Dátum: {formatDateTime(selectedMatch.utcDate)}
              </p>
              {existingBet ? (
                <p className="mb-4">
                  Existing bet: {existingBet.homeScoreBet} -{" "}
                  {existingBet.awayScoreBet}
                </p>
              ) : (
                <p className="mb-4">You didn't bet on this match, yet.</p>
              )}
              <div className="space-y-4">
                <div className="flex items-center space-x-2">
                  <label className="w-32">Home goals:</label>
                  <input
                    type="number"
                    min="0"
                    value={homeScoreBet}
                    onChange={(e) => setHomeScoreBet(Number(e.target.value))}
                    className="border rounded p-1 w-16"
                  />
                </div>
                <div className="flex items-center space-x-2">
                  <label className="w-32">Away goals:</label>
                  <input
                    type="number"
                    min="0"
                    value={awayScoreBet}
                    onChange={(e) => setAwayScoreBet(Number(e.target.value))}
                    className="border rounded p-1 w-16"
                  />
                </div>
              </div>
              <div className="mt-6 flex space-x-2">
                <button
                  onClick={handleBetSubmit}
                  className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                >
                  {existingBet ? "Update bet" : "Make bet"}
                </button>
                {existingBet && (
                  <button
                    onClick={handleCancelBet}
                    className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                  >
                    Cancel bet
                  </button>
                )}
                <button
                  onClick={closeBetModal}
                  className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                >
                  Close
                </button>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default Home;
