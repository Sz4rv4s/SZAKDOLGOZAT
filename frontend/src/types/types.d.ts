declare interface LoginCredentials {
  username: string;
  password: string;
}

declare interface RegisterCredentials extends LoginCredentials {
  email: string;
}

declare interface AuthTokens {
  accessToken: string;
  refreshToken: string;
}

declare interface UserProfile {
  accessToken: string;
  refreshToken: string;
  user: string;
  userId: number;
  roles: string[];
}

interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
  user: string | null;
  userId: string | null;
  roles: string | null;
  isAuthenticated: boolean;
  login: (username: string, password: string) => Promise<void>;
  register: (
    email: string,
    username: string,
    password: string,
  ) => Promise<void>;
  logout: () => Promise<void>;
  refresh: () => Promise<
    ApiResponse<{ accessToken: string; refreshToken: string }>
  >;
}

declare interface Competition {
  id: number;
  areaId: number;
  name: string;
  code: string;
  type: string;
  emblem: string;
  plan: string;
  currentSeasonId: number;
  numberOfAvailableSeasons: number;
  lastUpdated: string;
}

declare interface CompetitionResponse {
  success: boolean;
  message: string;
  competitions: Competition[];
}

declare interface Match {
  id: number;
  competitionId: number;
  competitionName: string;
  utcDate: string;
  status: string;
  homeTeamId: number;
  awayTeamId: number;
  homeTeamShortName: string;
  awayTeamShortName: string;
  homeTeamCrestUrl: string;
  awayTeamCrestUrl: string;
  winningTeamId: number | null;
  winningTeamShortName: string | null;
  homeGoals: number | null;
  awayGoals: number | null;
}

declare interface MatchResponse {
  success: boolean;
  message: string;
  match: Match[];
}

declare interface DefaultResponse {
  success: boolean;
  message: string;
}

export type ApiResponse<T> =
  | { success: true; data: T; error: null }
  | { success: false; data: null; error: string };

declare interface NotificationState {
  message: string | null;
  type: "success" | "error" | "info" | null;
  showNotification: (
    message: string,
    type: "success" | "error" | "info",
  ) => void;
  clearNotification: () => void;
}

export interface ApiErrorResponse {
  message?: string;
  [key: string]: string | null;
}

declare interface MatchScoreBetDTO {
  id: number;
  userId: number;
  userName: string;
  matchId: number;
  homeTeamId: number;
  awayTeamId: number;
  homeTeamShortName: string;
  awayTeamShortName: string;
  homeScoreBet: number;
  awayScoreBet: number;
  homeScore: number | null;
  awayScore: number | null;
  status: string;
  points: number | null;
  dateOfBet: string;
  dateOfMatch: string;
}

declare interface MatchScoreBetResponseDTO {
  success: boolean;
  message: string;
  matchScoreBet: MatchScoreBetDTO[];
}

declare type GetMatchScoreBetResponse =
  | MatchScoreBetResponseDTO
  | DefaultResponse;

declare interface LeaderboardEntry {
  userId: number;
  totalPoints: number;
}

declare interface UserResponseDTO {
  id: number;
  username: string;
  email: string;
}

declare interface LeaderboardUser extends LeaderboardEntry {
  username: string;
}
