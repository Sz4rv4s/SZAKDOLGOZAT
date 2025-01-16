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
          bsonType: "string",
          description: "The start date of the season in ISO format"
        },
        endDate: {
          bsonType: "string",
          description: "The end date of the season in ISO format"
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
          bsonType: "int",
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
          enum: ["CUP", "LEAGUE"],
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
          bsonType: ["string", "null"],
          description: "The timestamp of the last update in ISO format"
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
          bsonType: "int",
          description: "Reference to the league where the team plays"
        },
        seasonId: {
          bsonType: "int",
          description: "Reference to the current season's id"
        },
        areaId: {
          bsonType: "int",
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
      required: ["_id", "name", "position", "dateOfBirth", "nationality"],
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
          bsonType: "string",
          description: "The player's date of birth in ISO format"
        },
        nationality: {
          bsonType: "string",
          description: "The player's nationality"
        }
      }
    }
  }
});

db.competitions.createIndex({ "areaId": 1 });
db.competitions.createIndex({ "currentSeasonId": 1});
db.teams.createIndex({ "areaId": 1 });
