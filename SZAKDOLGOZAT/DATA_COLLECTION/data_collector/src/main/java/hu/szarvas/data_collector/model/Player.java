package hu.szarvas.data_collector.model;

import lombok.Data;

@Data
public class Player {
    private String playerKey;
    private String playerId;
    private String playerImage;
    private String playerName;
    private String playerCompleteName;
    private String playerNumber;
    private String playerCountry;
    private String playerType;
    private String playerAge;
}
