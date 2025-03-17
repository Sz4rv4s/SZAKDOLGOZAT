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
    const betResult = await getMatchScoreBet(match.id);
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
      ? updateMatchScoreBet(selectedMatch.id, { homeScoreBet, awayScoreBet })
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
    const result = await cancelMatchScoreBet(selectedMatch.id);
    if (result.success) {
      alert(result.data?.message || "Bet cancelled successfully");
      closeBetModal();
    } else {
      alert(result.error || "Failed to cancel bet");
    }
  };

  return (
    <div>
      <h1>Upcoming matches</h1>
      <ul>
        {matches.map((match) => (
          <li key={match.id}>
            {match.homeTeamShortName} vs {match.awayTeamShortName} -{" "}
            {formatDateTime(match.utcDate)}{" "}
            <button onClick={() => openBetModal(match)}>Bet</button>
          </li>
        ))}
      </ul>

      {selectedMatch && (
        <div
          style={{
            position: "fixed",
            top: "20%",
            left: "20%",
            background: "white",
            padding: "20px",
            border: "1px solid black",
          }}
        >
          <h2>
            Create bet: {selectedMatch.homeTeamShortName} vs{" "}
            {selectedMatch.awayTeamShortName}
          </h2>
          <p>Dátum: {formatDateTime(selectedMatch.utcDate)}</p>
          {existingBet ? (
            <p>
              Existing bet: {existingBet.homeScoreBet} -{" "}
              {existingBet.awayScoreBet}
            </p>
          ) : (
            <p>There is no bet for this match, yet.</p>
          )}
          <div>
            <label>
              Home goals:
              <input
                type="number"
                min="0"
                value={homeScoreBet}
                onChange={(e) => setHomeScoreBet(Number(e.target.value))}
              />
            </label>
            <label>
              Away goals:
              <input
                type="number"
                min="0"
                value={awayScoreBet}
                onChange={(e) => setAwayScoreBet(Number(e.target.value))}
              />
            </label>
          </div>
          <button onClick={handleBetSubmit}>
            {existingBet ? "Update" : "Create bet"}
          </button>
          {existingBet && <button onClick={handleCancelBet}>Delete bet</button>}
          <button onClick={closeBetModal}>Close</button>
        </div>
      )}
    </div>
  );
};

export default Home;
