package DataStructures;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    public enum Color {NONE, RED, BLUE }

    private String name;
    private UUID id;
    private Color color;

    public Player(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
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
