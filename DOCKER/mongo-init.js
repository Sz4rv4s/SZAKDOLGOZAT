db = db.getSiblingDB('footballdb')
db.createUser({
  user: process.env.MONGO_USER,
  pwd: process.env.MONGO_PASSWORD,
  roles: [{ role: 'readWrite', db: 'footballdb' }]
})

db.createCollection("areas", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "name", "countryCode"],
      properties: {
        _id: {
          bsonType: "int",
          description: "The unique identifier of the area"
        },
        name: {
          bsonType: "string",
          description: "The name of the area (country or continent)"
        },
        countryCode: {
          bsonType: "string",
          description: "The 3 letter code of the country"
        },
        flag: {
          bsonType: ["string", "null"],
          description: "The URL to the country's flag image"
        },
        parentAreaId: {
          bsonType: ["int", "null"],
          description: "The ID of the area's parent (continent)"
        },
        parentArea: {
          bsonType: ["string", "null"],
          description: "The name of the area's parent (continent)"
        }
      }
    }
  }
});

db.createCollection("seasons", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "startDate", "endDate"],
      properties: {
        _id: {
          bsonType: "int",
          description: "The unique identifier of the season"
        },
        startDate: {
          bsonType: "date",
          description: "The start date of the season"
        },
        endDate: {
          bsonType: "date",
          description: "The end date of the season"
        },
        currentMatchday: {
          bsonType: ["int", "null"],
          description: "The current matchday number of the season"
        },
        winner: {
          bsonType: ["string", "null"],
          description: "The name of the season's winner, if concluded"
        }
      }
    }
  }
});

db.createCollection("competitions", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "areaId", "name", "code", "type", "plan"],
      properties: {
        _id: {
          bsonType: "int",
          description: "The unique identifier of the competition"
        },
        areaId: {
          bsonType: ["int", "null"],
          description: "Reference to the area where the competition takes place"
        },
        name: {
          bsonType: "string",
          description: "The full name of the competition"
        },
        code: {
          bsonType: "string",
          description: "The unique code identifier of the competition"
        },
        type: {
          bsonType: "string",
          enum: ["CUP", "LEAGUE", "SUPER_CUP", "PLAYOFFS"],
          description: "The type of the competition"
        },
        emblem: {
          bsonType: ["string", "null"],
          description: "The URL to the competition's emblem"
        },
        plan: {
          bsonType: "string",
          enum: ["TIER_ONE", "TIER_TWO", "TIER_THREE", "TIER_FOUR"],
          description: "The tier level of the competition"
        },
        currentSeasonId: {
          bsonType: ["int", "null"],
          description: "Reference to the current season of the competition"
        },
        numberOfAvailableSeasons: {
          bsonType: ["int", "null"],
          description: "The number of available seasons for this competition"
        },
        lastUpdated: {
          bsonType: ["date", "null"],
          description: "The timestamp of the last update"
        }
      }
    }
  }
});

db.createCollection("teams", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "competitionId", "seasonId", "areaId", "name", "playerIds"],
      properties: {
        _id: {
          bsonType: "int",
          description: "The unique identifier of the team"
        },
        competitionId: {
          bsonType: ["int", "null"],
          description: "Reference to the league where the team plays"
        },
        seasonId: {
          bsonType: ["int", "null"],
          description: "Reference to the current season's id"
        },
        areaId: {
          bsonType: ["int", "null"],
          description: "Reference to the area where the team is based"
        },
        name: {
          bsonType: "string",
          description: "The full name of the team"
        },
        shortName: {
          bsonType: "string",
          description: "The abbreviated or common name of the team"
        },
        tla: {
          bsonType: "string",
          description: "The three-letter abbreviation of the team"
        },
        crest: {
          bsonType: ["string", "null"],
          description: "The URL to the team's crest image"
        },
        squad: {
          bsonType: "array",
          description: "List of player IDs in the team's squad",
          items: {
            bsonType: "int"
          }
        }
      }
    }
  }
});

db.createCollection("players", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "name"],
      properties: {
        _id: {
          bsonType: "int",
          description: "The unique identifier of the player"
        },
        name: {
          bsonType: "string",
          description: "The full name of the player"
        },
        position: {
          bsonType: "string",
          description: "The playing position of the player"
        },
        dateOfBirth: {
          bsonType: ["date", "null"],
          description: "The player's date of birth in ISO format"
        },
        nationality: {
          bsonType: ["string", "null"],
          description: "The player's nationality"
        }
      }
    }
  }
});

db.createCollection("matches", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "competitionId", "seasonId", "utcDate", "status", "homeTeamId", "awayTeamId"],
      properties: {
        _id: {
          bsonType: "int",
          description: "The unique identifier of the match"
        },
        competitionId: {
          bsonType: ["int", "null"],
          description: "Reference to the competition's ID where the match is played"
        },
        seasonId: {
          bsonType: ["int", "null"],
          description: "Reference to the season's ID when the match is played"
        },
        utcDate: {
          bsonType: "date",
          description: "The date when the match will/was/is played in UTC"
        },
        status: {
          bsonType: "string",
          enum: ["FINISHED", "SCHEDULED", "TIMED", "LIVE", "IN_PLAY", "PAUSED", "POSTPONED", "SUSPENDED", "CANCELLED"],
          description: "The status of the match"
        },
        matchday: {
          bsonType: "int",
          description: "The matchday when the match is played"
        },
        lastUpdated: {
          bsonType: "date",
          description: "The date when the match data was last updated"
        },
        homeTeamId: {
          bsonType: ["int", "null"],
          description: "Reference to the home team's ID"
        },
        awayTeamId: {
          bsonType: ["int", "null"],
          description: "Reference to the away team's ID"
        },
        winner: {
          bsonType: "string",
          enum: ["HOME_TEAM", "AWAY_TEAM", "DRAW", "null"],
          description: "The result of the match"
        },
        homeGoals: {
          bsonType: ["int", "null"],
          description: "The goal number of the home team"
        },
        awayGoals: {
          bsonType: ["int", "null"],
          description: "The goal number of the away team"
        },
      }
    }
  }
});

db.createCollection("users", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "email", "username", "password"],
      properties: {
        _id: {
          bsonType: "int",
          description: "The unique identifier of the user"
        },
        email: {
          bsonType: "string",
          description: "The unique email of the user"
        },
        username: {
          bsonType: "string",
          description: "The unique username of the user"
        },
        password: {
          bsonType: "string",
          description: "The password of the user"
        },
        refreshToken: {
          bsonType: ["string", "null"],
          description: "The refresh token of the user if logged in"
        }
      }
    }
  }
});

db.createCollection("match_score_bets", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "userId", "matchId", "homeScoreBet", "awayScoreBet", "status"],
      properties: {
        _id: {
          bsonType: "int",
          description: "The unique identifier of the match score bet"
        },
        userId: {
          bsonType: "int",
          description: "Reference to the user"
        },
        matchId: {
          bsonType: "int",
          description: "Reference to the match"
        },
        homeScoreBet: {
          bsonType: "int",
          description: "The number of goals the home team will score"
        },
        awayScoreBet: {
          bsonType: "int",
          description: "The number of goals the away team will score"
        },
        status: {
          bsonType: "string",
          enum: ["FINISHED", "LIVE", "IN_PLAY", "CANCELLED"],
          description: "The status of the bet"
        },
        date: {
          bsonType: "date",
          description: "The date when the bet is made"
        }
      }
    }
  }
});

db.createCollection("user_points", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "userId", "points", "matchId", "matchDate"],
      properties: {
        _id: {
          bsonType: "int",
          description: "The unique identifier of the point entry"
        },
        userId: {
          bsonType: "int",
          description: "Reference to the user"
        },
        points: {
          bsonType: "int",
          description: "The amount of points the user gained"
        },
        matchId: {
          bsonType: "int",
          description: "Reference to the match"
        },
        matchDate: {
          bsonType: "date",
          description: "The date of the match"
        }
      }
    }
  }
});
