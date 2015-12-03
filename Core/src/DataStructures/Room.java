package DataStructures;

import java.io.Serializable;
import java.util.UUID;

public class Room implements Serializable {
    private String name;
    private UUID id;
    private Board board;
    private Player bluePlayer;
    private Player redPlayer;
    private Scores scores;

    public Room(String name, int size, Player bluePlayer) {
        this.name = name;
        board = new Board(size);
        this.bluePlayer = bluePlayer;
        this.id = UUID.randomUUID();
        scores = new Scores(0, 0);
    }

    public Room(Room room) {
        name = room.getName();
        board = new Board(room.getBoard());
        bluePlayer = room.getBluePlayer();
        redPlayer = room.getRedPlayer();
        id = room.getId();
        scores = room.getScores();
    }

    public Board getBoard() {
        return board;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public int getSize() {
        return board.getSize();
    }

    public Player getBluePlayer() {
        return bluePlayer;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public void unbindPlayer(Player player) {
        if (bluePlayer == player) {
            bluePlayer = null;
        } else if (redPlayer == player) {
            redPlayer = null;
        }
    }

    public void setBluePlayer(Player bluePlayer) {
        this.bluePlayer = bluePlayer;
    }

    public void setRedPlayer(Player redPlayer) {
        this.redPlayer = redPlayer;
    }

    public void setScores(Scores scores) {
        this.scores = scores;
    }

    public Scores getScores() {
        return scores;
    }

    public boolean isFree() {
        return bluePlayer == null || redPlayer == null;
    }
}
