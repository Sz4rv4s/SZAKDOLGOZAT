import { FC, useState } from "react";
import { useAuthStore } from "../store/authStore";
import { useNavigate } from "react-router-dom";
import {
  fetchAreas,
  fetchCompetitionsWithSeasons,
  fetchTeamsWithPlayers,
  fetchMatches,
} from "../api/footballApi.ts";
import { ApiResponse } from "../types/types";

export const Admin: FC = () => {
  const { roles, isAuthenticated } = useAuthStore();
  const navigate = useNavigate();
  const [loadingAreas, setLoadingAreas] = useState(false);
  const [loadingCompetitions, setLoadingCompetitions] = useState(false);
  const [loadingTeams, setLoadingTeams] = useState(false);
  const [loadingMatches, setLoadingMatches] = useState(false);
  const [error, setError] = useState<string | null>(null);

  if (!isAuthenticated || roles !== "ADMIN") {
    navigate("/login");
    return null;
  }

  const handleFetchAreas = async () => {
    setLoadingAreas(true);
    setError(null);
    const result: ApiResponse<void> = await fetchAreas();
    setLoadingAreas(false);
    if (!result.success) {
      setError(result.error || "Failed to fetch areas");
    }
  };

  const handleFetchCompetitions = async () => {
    setLoadingCompetitions(true);
    setError(null);
    const result: ApiResponse<void> = await fetchCompetitionsWithSeasons();
    setLoadingCompetitions(false);
    if (!result.success) {
      setError(result.error || "Failed to fetch competitions with seasons");
    }
  };

  const handleFetchTeams = async () => {
    setLoadingTeams(true);
    setError(null);
    const result: ApiResponse<void> = await fetchTeamsWithPlayers();
    setLoadingTeams(false);
    if (!result.success) {
      setError(result.error || "Failed to fetch teams with players");
    }
  };

  const handleFetchMatches = async () => {
    setLoadingMatches(true);
    setError(null);
    const result: ApiResponse<void> = await fetchMatches();
    setLoadingMatches(false);
    if (!result.success) {
      setError(result.error || "Failed to fetch matches");
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Admin Panel</h1>

      {error && (
        <div className="mb-4 p-4 bg-red-100 text-red-700 rounded-lg">
          {error}
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <button
          onClick={handleFetchAreas}
          disabled={loadingAreas}
          className={`w-full py-3 px-4 rounded-lg text-white font-semibold ${
            loadingAreas
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-blue-500 hover:bg-blue-600"
          } flex items-center justify-center`}
        >
          {loadingAreas ? (
            <>
              <div className="animate-spin rounded-full h-5 w-5 border-t-2 border-b-2 border-white mr-2"></div>
              Fetching Areas...
            </>
          ) : (
            "Fetch Areas"
          )}
        </button>

        <button
          onClick={handleFetchCompetitions}
          disabled={loadingCompetitions}
          className={`w-full py-3 px-4 rounded-lg text-white font-semibold ${
            loadingCompetitions
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-blue-500 hover:bg-blue-600"
          } flex items-center justify-center`}
        >
          {loadingCompetitions ? (
            <>
              <div className="animate-spin rounded-full h-5 w-5 border-t-2 border-b-2 border-white mr-2"></div>
              Fetching Competitions...
            </>
          ) : (
            "Fetch Competitions with Seasons"
          )}
        </button>

        <button
          onClick={handleFetchTeams}
          disabled={loadingTeams}
          className={`w-full py-3 px-4 rounded-lg text-white font-semibold ${
            loadingTeams
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-blue-500 hover:bg-blue-600"
          } flex items-center justify-center`}
        >
          {loadingTeams ? (
            <>
              <div className="animate-spin rounded-full h-5 w-5 border-t-2 border-b-2 border-white mr-2"></div>
              Fetching Teams...
            </>
          ) : (
            "Fetch Teams with Players"
          )}
        </button>

        <button
          onClick={handleFetchMatches}
          disabled={loadingMatches}
          className={`w-full py-3 px-4 rounded-lg text-white font-semibold ${
            loadingMatches
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-blue-500 hover:bg-blue-600"
          } flex items-center justify-center`}
        >
          {loadingMatches ? (
            <>
              <div className="animate-spin rounded-full h-5 w-5 border-t-2 border-b-2 border-white mr-2"></div>
              Fetching Matches...
            </>
          ) : (
            "Fetch Matches"
          )}
        </button>
      </div>
    </div>
  );
};

export default Admin;
