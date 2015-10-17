package DataStructures;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by VladVin on 17.10.2015.
 */
public class Player implements Serializable {
    private String name;
    private UUID id;

    public Player(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }
}
