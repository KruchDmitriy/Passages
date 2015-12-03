import DataStructures.Board;
import DataStructures.BoardChange;
import Interfaces.IClient;

import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Created by VladVin on 03.12.2015.
 */
public class Player extends Thread {
    private enum Command {NONE, TERMINATE, SET_COLOR}

    private String playerName;
    private UUID id;
    private DataStructures.Player.Color color;

    private IClient client;
    private Command currentCommand = Command.NONE;
    private volatile boolean running = true;

    public Player(String name, UUID id, IClient client) {
        this.playerName = name;
        this.id = id;
        this.client = client;
    }

    @Override
    public void run() {
        super.run();

        while (running) {
            try {
                currentCommand.wait();
                switch (currentCommand) {
                    case SET_COLOR:
                        try {
                            client.setPlayerColor(color);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case TERMINATE:
                        return;
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void terminate() {
        running = false;
        currentCommand = Command.TERMINATE;
        currentCommand.notify();
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
        currentCommand = Command.SET_COLOR;
        currentCommand.notify();
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
