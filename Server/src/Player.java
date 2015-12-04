import DataStructures.Board;
import DataStructures.BoardChange;
import Interfaces.IClient;

import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Created by VladVin on 03.12.2015.
 */
public class Player{

    private String playerName;
    private UUID id;
    private DataStructures.Player.Color color;

    private IClient client;

    public Player(String name, UUID id, IClient client) {
        this.playerName = name;
        this.id = id;
        this.client = client;
    }

    public String getPlayerName() {
        return playerName;
    }

    public DataStructures.Player.Color getColor() {
        return color;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setColor(DataStructures.Player.Color color) {
        this.color = color;
        try {
            client.setPlayerColor(color);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        try {
            client.startGame();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateBoard(BoardChange boardChange) {
        try {
            client.updateBoard(boardChange);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
