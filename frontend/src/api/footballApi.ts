import api from "./api";
import {
  ApiResponse,
  CompetitionResponse,
  DefaultResponse,
  GetMatchScoreBetResponse,
  LeaderboardEntry,
  MatchResponse,
  UserResponseDTO,
} from "../types/types";

export const getLeagues = async (): Promise<
  ApiResponse<CompetitionResponse>
> => {
  try {
    console.log("Fetching leagues");
    const response = await api.get("/football/get/leagues");
    console.log("Leagues response:", response.data);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    console.error("Error fetching leagues:", error);
    return { success: false, data: null, error: String(error) };
  }
};

export const getUpcomingMatchesForLeague = async (
  leagueId: number,
): Promise<ApiResponse<MatchResponse>> => {
  try {
    const response = await api.get(
      `/football/get/${leagueId}/upcoming-matches`,
    );
    console.log(`Matches for league ${leagueId}:`, response.data);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    console.error(`Error fetching matches for league ${leagueId}:`, error);
    return { success: false, data: null, error: String(error) };
  }
};

export const makeMatchScoreBet = async (bet: {
  userId: number;
  matchId: number;
  homeScoreBet: number;
  awayScoreBet: number;
  status: string;
  date: string;
}): Promise<ApiResponse<DefaultResponse>> => {
  try {
    const response = await api.post("/football/bets/match-score", bet);
    console.log("Make bet response:", response.data);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    console.error("Error making bet:", error);
    return { success: false, data: null, error: String(error) };
  }
};

export const getMatchScoreBet = async (
  userId: number,
  matchId: number,
): Promise<ApiResponse<GetMatchScoreBetResponse>> => {
  try {
    console.log(`Fetching bet for user ${userId}, match ${matchId}`);
    const response = await api.get(
      `/football/bets/match-score/${userId}/${matchId}`,
    );
    console.log("Get bet response:", response.data);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    console.error(
      `Error fetching bet for user ${userId}, match ${matchId}:`,
      error,
    );
    return { success: false, data: null, error: String(error) };
  }
};

export const updateMatchScoreBet = async (
  userId: number,
  bet: {
    matchId: number;
    homeScoreBet: number;
    awayScoreBet: number;
  },
): Promise<ApiResponse<DefaultResponse>> => {
  try {
    const response = await api.patch(
      `/football/bets/match-score/${userId}`,
      bet,
    );
    console.log("Update bet response:", response.data);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    console.error("Error updating bet:", error);
    return { success: false, data: null, error: String(error) };
  }
};

export const cancelMatchScoreBet = async (
  userId: number,
  matchId: number,
): Promise<ApiResponse<DefaultResponse>> => {
  try {
    const response = await api.delete(
      `/football/bets/match-score/${userId}/${matchId}`,
    );
    console.log("Cancel bet response:", response.data);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    console.error("Error canceling bet:", error);
    return { success: false, data: null, error: String(error) };
  }
};

export const getWeeklyLeaderboard = async (): Promise<
  ApiResponse<LeaderboardEntry[]>
> => {
  try {
    const response = await api.get(`/leaderboard/weekly`);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    return {
      success: false,
      data: null,
      error: String(error),
    };
  }
};

export const getMonthlyLeaderboard = async (): Promise<
  ApiResponse<LeaderboardEntry[]>
> => {
  try {
    const response = await api.get(`/leaderboard/monthly`);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    return {
      success: false,
      data: null,
      error: String(error),
    };
  }
};

export const getUser = async (
  userId: number,
): Promise<ApiResponse<UserResponseDTO>> => {
  try {
    const response = await api.get(`/user/${userId}`);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    return {
      success: false,
      data: null,
      error: String(error),
    };
  }
};
