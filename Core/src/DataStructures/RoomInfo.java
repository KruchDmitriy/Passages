package DataStructures;

import java.io.Serializable;
import java.util.UUID;

public class RoomInfo implements Serializable {
    private String name;
    private UUID id;
    private int boardSize;
    private Player bluePlayer;
    private Player redPlayer;

    public RoomInfo(String name, UUID id, int boardSize, Player bluePlayer, Player redPlayer) {
        this.name = name;
        this.id = id;
        this.boardSize = boardSize;
        this.bluePlayer = bluePlayer;
        this.redPlayer = redPlayer;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Player getBluePlayer() {
        return bluePlayer;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }
}
