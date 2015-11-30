package DataStructures;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    private String name;
    private UUID id;

    public Player(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
}
