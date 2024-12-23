db = db.getSiblingDB('football_db');

db.createCollection("countries", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["country_id", "country_name", "country_logo"],
      properties: {
        country_id: {
          bsonType: "objectId",
          description: "The ID of the country - required"
        },
        country_name: {
          bsonType: "string",
          description: "The name of the country - required"
        },
        country_logo: {
          bsonType: "string",
          description: "The URL of the country's flag - required"
        }
      }
    }
  }
});

db.createCollection("leagues", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["country_id", "country_name", "league_id", "league_name, league_season", "league_logo", "country_logo"],
      properties: {
        country_id: {
          bsonType: "objectId",
          description: "Reference to ID of the country - required"
        },
        country_name: {
          bsonType: "string",
          description: "The name of the country - required"
        },
        league_id: {
          bsonType: "objectId",
          description: "The ID of the league - required"
        },
        league_name: {
          bsonType: "string",
          description: "The name of the league - required"
        },
        league_season: {
          bsonType: "string",
          description: "The season of the league - required"
        },
        league_logo: {
          bsonType: "string",
          description: "The URL of the league's logo - required"
        },
        country_logo: {
          bsonType: "string",
          description: "The URL of the country's flag - required"
        }
      }
    }
  }
});

db.createCollection("teams", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["team_key", "team_name", "team_country"],
      properties: {
        team_key: {
          bsonType: "string",
          description: "Team unique identifier"
        },
        team_name: {
          bsonType: "string",
          description: "Name of the team"
        },
        team_country: {
          bsonType: "string",
          description: "Country of the team"
        },
        team_founded: {
          bsonType: "string",
          description: "Year team was founded"
        },
        team_badge: {
          bsonType: "string",
          description: "URL to team badge image"
        },
        venue: {
          bsonType: "object",
          required: ["venue_name"],
          properties: {
            venue_name: { bsonType: "string" },
            venue_address: { bsonType: "string" },
            venue_city: { bsonType: "string" },
            venue_capacity: { bsonType: "string" },
            venue_surface: { bsonType: "string" }
          }
        },
        players: {
          bsonType: "array",
          items: {
            bsonType: "object",
            required: ["player_key", "player_name"],
            properties: {
              player_key: { bsonType: "string" },
              player_id: { bsonType: "string" },
              player_image: { bsonType: "string" },
              player_name: { bsonType: "string" },
              player_complete_name: { bsonType: "string" },
              player_number: { bsonType: "string" },
              player_country: { bsonType: "string" },
              player_type: { bsonType: "string" },
              player_age: { bsonType: "string" },
              player_match_played: { bsonType: "string" },
              player_goals: { bsonType: "string" },
              player_yellow_cards: { bsonType: "string" },
              player_red_cards: { bsonType: "string" },
              player_injured: { bsonType: "string" },
              player_substitute_out: { bsonType: "string" },
              player_substitutes_on_bench: { bsonType: "string" },
              player_assists: { bsonType: "string" },
              player_birthdate: { bsonType: "string" },
              player_rating: { bsonType: "string" }
            }
          }
        },
        coaches: {
          bsonType: "array",
          items: {
            bsonType: "object",
            required: ["coach_name"],
            properties: {
              coach_name: { bsonType: "string" },
              coach_country: { bsonType: "string" },
              coach_age: { bsonType: "string" }
            }
          }
        }
      }
    }
  }
});


db.countries.createIndex({ "country_id": 1 }, { unique: true });
db.leagues.createIndex({ "league_id": 1 }, { unique: true });
db.teams.createIndex({ "team_key": 1 }, { unique: true });


db.createUser({
  user: 'game_api_user',
  pwd: 'game_api_password',
  roles: [
    {
      role: 'readWrite',
      db: 'football_db'
    }
  ]
});
