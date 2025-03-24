import api from "./api";
import {
  ApiResponse,
  CompetitionResponse,
  DefaultResponse,
  MatchResponse,
} from "../types/types";

export const getLeagues = async (): Promise<
  ApiResponse<CompetitionResponse>
> => {
  try {
    console.log("Fetching leagues");
    const response = await api.get("/football/get/leagues");
    console.log("Response:", response.data);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    console.log("Error:", error);
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
    return { success: true, data: response.data, error: null };
  } catch (error) {
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
    console.log("Request: ", bet);
    const response = await api.post("/football/bets/match-score", bet);
    return { success: true, data: response.data, error: null };
  } catch (error) {
    return { success: false, data: null, error: String(error) };
  }
};

export const getMatchScoreBet = async (
  matchId: number,
  userId: number,
): Promise<
  ApiResponse<{ bet: { homeScoreBet: number; awayScoreBet: number } | null }>
> => {
  try {
    const response = await api.get(
      `/football/bets/match-score/${userId}/${matchId}`,
    );
    return { success: true, data: response.data, error: null };
  } catch (error) {
    return { success: false, data: null, error: String(error) };
  }
};

export const updateMatchScoreBet = async (
  matchId: number,
  userId: number,
  bet: { homeScoreBet: number; awayScoreBet: number },
): Promise<ApiResponse<DefaultResponse>> => {
  try {
    const response = await api.put(
      `/football/bets/match-score/${userId}/${matchId}`,
      bet,
    );
    return { success: true, data: response.data, error: null };
  } catch (error) {
    return { success: false, data: null, error: String(error) };
  }
};

export const cancelMatchScoreBet = async (
  matchId: number,
  userId: number,
): Promise<ApiResponse<DefaultResponse>> => {
  try {
    const response = await api.delete(
      `/football/bets/match-score/${userId}/${matchId}`,
    );
    return { success: true, data: response.data, error: null };
  } catch (error) {
    return { success: false, data: null, error: String(error) };
  }
};
