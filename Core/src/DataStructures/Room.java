package DataStructures;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by VladVin on 17.10.2015.
 */
public class Room implements Serializable {
    private String name;
    private UUID id;
    private int nsize;
    private Player bluePlayer;
    private Player redPlayer;

    public Room(String name, int nsize, Player bluePlayer) {
        this.name = name;
        this.nsize = nsize;
        this.bluePlayer = bluePlayer;
        this.id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public int getNsize() {
        return nsize;
    }

    public Player getBluePlayer() {
        return bluePlayer;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public void setNsize(int nsize) {
        this.nsize = nsize;
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
