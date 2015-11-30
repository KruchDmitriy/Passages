package DataStructures;

import java.io.Serializable;
import java.util.UUID;

public class Room implements Serializable {
    private String name;
    private UUID id;
    private int size;
    private Player bluePlayer;
    private Player redPlayer;

    public Room(String name, int size, Player bluePlayer) {
        this.name = name;
        this.size = size;
        this.bluePlayer = bluePlayer;
        this.id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public Player getBluePlayer() {
        return bluePlayer;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setBluePlayer(Player bluePlayer) {
        this.bluePlayer = bluePlayer;
    }

    public void setRedPlayer(Player redPlayer) {
        this.redPlayer = redPlayer;
    }

    public void setName(String name) {
        this.name = name;
    }
}
