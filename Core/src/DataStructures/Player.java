package DataStructures;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    public enum Color { RED, BLUE }

    private String name;
    private UUID id;
    private Color color;

    public Player(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
