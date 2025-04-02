import { FC, useEffect, useState } from "react";
import {
  cancelMatchScoreBet,
  getLeagues,
  getMatchScoreBet,
  getUpcomingMatchesForLeague,
  makeMatchScoreBet,
  updateMatchScoreBet,
} from "../api/footballApi.ts";
import {
  Competition,
  Match,
  GetMatchScoreBetResponse,
  ApiResponse,
} from "../types/types";
import { useAuthStore } from "../store/authStore.ts";

export const Home: FC = () => {
  const [leagues, setLeagues] = useState<Competition[]>([]);
  const [matchesByLeague, setMatchesByLeague] = useState<
    Record<number, Match[]>
  >({});
  const [betsByMatch, setBetsByMatch] = useState<
    Record<number, { homeScoreBet: number; awayScoreBet: number }>
  >({});
  const [selectedMatch, setSelectedMatch] = useState<Match | null>(null);
  const [homeScoreBet, setHomeScoreBet] = useState<number>(0);
  const [awayScoreBet, setAwayScoreBet] = useState<number>(0);
  const [existingBet, setExistingBet] = useState<{
    homeScoreBet: number;
    awayScoreBet: number;
  } | null>(null);
  const [isLoadingLeagues, setIsLoadingLeagues] = useState(true);
  const [loadingMatches, setLoadingMatches] = useState<Set<number>>(new Set());
  const [loadingBets, setLoadingBets] = useState<Set<number>>(new Set());
  const { userId } = useAuthStore();

  useEffect(() => {
    const fetchData = async () => {
      setIsLoadingLeagues(true);
      const leaguesResult = await getLeagues();
      setIsLoadingLeagues(false);

      if (leaguesResult.success && leaguesResult.data?.competitions) {
        setLeagues(leaguesResult.data.competitions);

        for (const league of leaguesResult.data.competitions) {
          setLoadingMatches((prev) => new Set(prev).add(league.id));

          const matchesResult = await getUpcomingMatchesForLeague(league.id);
          if (matchesResult.success && matchesResult.data?.match) {
            setMatchesByLeague((prev) => ({
              ...prev,
              [league.id]: matchesResult.data.match.slice(0, 5),
            }));

            if (userId) {
              const matches = matchesResult.data.match.slice(0, 5);
              for (const match of matches) {
                setLoadingBets((prev) => new Set(prev).add(match.id));
                const betResult = await getMatchScoreBet(
                  Number(userId),
                  match.id,
                );
                if (betResult.success && betResult.data) {
                  if (
                    "matchScoreBet" in betResult.data &&
                    betResult.data.matchScoreBet.length > 0
                  ) {
                    const bet = betResult.data.matchScoreBet[0];
                    setBetsByMatch((prev) => ({
                      ...prev,
                      [match.id]: {
                        homeScoreBet: bet.homeScoreBet,
                        awayScoreBet: bet.awayScoreBet,
                      },
                    }));
                  }
                }
                setLoadingBets((prev) => {
                  const newSet = new Set(prev);
                  newSet.delete(match.id);
                  return newSet;
                });
              }
            }
          }
          setLoadingMatches((prev) => {
            const newSet = new Set(prev);
            newSet.delete(league.id);
            return newSet;
          });
        }
      }
    };
    fetchData().then(() => {});
  }, [userId]);

  const nowDateTime = (): string => {
    return new Date().toISOString();
  };

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

  const openBetModal = async (match: Match) => {
    setSelectedMatch(match);
    setHomeScoreBet(0);
    setAwayScoreBet(0);
    if (!userId) {
      console.error("No userId available");
      return;
    }
    const betResult = await getMatchScoreBet(Number(userId), match.id);
    console.log("Bet result:", betResult);

    if (betResult.success && betResult.data) {
      if (
        "matchScoreBet" in betResult.data &&
        betResult.data.matchScoreBet.length > 0
      ) {
        const bet = betResult.data.matchScoreBet[0];
        setExistingBet({
          homeScoreBet: bet.homeScoreBet,
          awayScoreBet: bet.awayScoreBet,
        });
        setHomeScoreBet(bet.homeScoreBet);
        setAwayScoreBet(bet.awayScoreBet);
      } else {
        console.log("No existing bet found:", betResult.data.message);
        setExistingBet(null);
      }
    } else {
      console.error("Error fetching bet:", betResult.error);
      setExistingBet(null);
    }
  };

  const closeBetModal = () => {
    setSelectedMatch(null);
    setExistingBet(null);
  };

  const fetchBetForMatch = async (matchId: number) => {
    if (!userId) return;
    setLoadingBets((prev) => new Set(prev).add(matchId));
    const betResult: ApiResponse<GetMatchScoreBetResponse> =
      await getMatchScoreBet(Number(userId), matchId);

    if (betResult.success && betResult.data) {
      if (
        "matchScoreBet" in betResult.data &&
        betResult.data.matchScoreBet.length > 0
      ) {
        const bet = betResult.data.matchScoreBet[0];
        setBetsByMatch((prev) => ({
          ...prev,
          [matchId]: {
            homeScoreBet: bet.homeScoreBet,
            awayScoreBet: bet.awayScoreBet,
          },
        }));
      } else {
        setBetsByMatch((prev) => {
          const newBets = { ...prev };
          delete newBets[matchId];
          return newBets;
        });
      }
    }
    setLoadingBets((prev) => {
      const newSet = new Set(prev);
      newSet.delete(matchId);
      return newSet;
    });
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
    const updateBet = {
      matchId: selectedMatch.id,
      homeScoreBet,
      awayScoreBet,
    };
    const result = await (existingBet
      ? updateMatchScoreBet(Number(userId), updateBet)
      : makeMatchScoreBet(bet));
    if (result.success) {
      alert(result.data?.message || "Bet processed successfully");
      await fetchBetForMatch(selectedMatch.id);
      closeBetModal();
    } else {
      alert(result.error || "Failed to process bet");
    }
  };

  const handleCancelBet = async () => {
    if (!selectedMatch || !userId) return;
    const result = await cancelMatchScoreBet(Number(userId), selectedMatch.id);
    if (result.success) {
      alert(result.data?.message || "Bet cancelled successfully");
      await fetchBetForMatch(selectedMatch.id);
      closeBetModal();
    } else {
      alert(result.error || "Failed to cancel bet");
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Upcoming Matches by League</h1>

      {isLoadingLeagues ? (
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      ) : (
        leagues.map((league) => (
          <div key={league.id} className="mb-8">
            <div className="flex items-center mb-4">
              <img
                src={league.emblem}
                alt={`${league.name} emblem`}
                className="w-12 h-12 mr-3"
                onError={(e) => (e.currentTarget.src = "/fallback-emblem.png")}
              />
              <h2 className="text-2xl font-semibold">{league.name}</h2>
            </div>

            {loadingMatches.has(league.id) ? (
              <div className="flex justify-center items-center h-32">
                <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
              </div>
            ) : (
              <div className="grid gap-4">
                {matchesByLeague[league.id]?.map((match) => (
                  <div
                    key={match.id}
                    className="flex items-center justify-between bg-gray-100 p-4 rounded-lg"
                  >
                    <div className="flex items-center space-x-4">
                      <img
                        src={match.homeTeamCrestUrl}
                        alt={`${match.homeTeamShortName} crest`}
                        className="w-8 h-8"
                        onError={(e) =>
                          (e.currentTarget.src = "/fallback-crest.png")
                        }
                      />
                      <span className="font-medium">
                        {match.homeTeamShortName}
                      </span>
                      <span>vs</span>
                      <span className="font-medium">
                        {match.awayTeamShortName}
                      </span>
                      <img
                        src={match.awayTeamCrestUrl}
                        alt={`${match.awayTeamShortName} crest`}
                        className="w-8 h-8"
                        onError={(e) =>
                          (e.currentTarget.src = "/fallback-crest.png")
                        }
                      />
                    </div>
                    <div className="flex items-center space-x-4">
                      <span>{formatDisplayDateTime(match.utcDate)}</span>
                      {loadingBets.has(match.id) ? (
                        <div className="animate-spin rounded-full h-6 w-6 border-t-2 border-b-2 border-blue-500"></div>
                      ) : betsByMatch[match.id] ? (
                        <span className="text-green-600">
                          Bet: {betsByMatch[match.id].homeScoreBet} -{" "}
                          {betsByMatch[match.id].awayScoreBet}
                        </span>
                      ) : null}
                      <button
                        onClick={() => openBetModal(match)}
                        className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                      >
                        Make Bet
                      </button>
                    </div>
                  </div>
                )) || <p>No upcoming matches available</p>}
              </div>
            )}
          </div>
        ))
      )}

      {selectedMatch && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
          <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
            <h2 className="text-xl font-semibold mb-4">
              Make bet: {selectedMatch.homeTeamShortName} vs{" "}
              {selectedMatch.awayTeamShortName}
            </h2>
            <div className="flex items-center space-x-2 mb-4">
              <img
                src={selectedMatch.homeTeamCrestUrl}
                alt="Home team crest"
                className="w-6 h-6"
              />
              <span>vs</span>
              <img
                src={selectedMatch.awayTeamCrestUrl}
                alt="Away team crest"
                className="w-6 h-6"
              />
            </div>
            <p className="mb-2">
              Date: {formatDisplayDateTime(selectedMatch.utcDate)}
            </p>
            {existingBet ? (
              <p className="mb-4">
                Existing bet: {existingBet.homeScoreBet} -{" "}
                {existingBet.awayScoreBet}
              </p>
            ) : (
              <p className="mb-4">You didn't bet on this match yet.</p>
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
      )}
    </div>
  );
};

export default Home;
