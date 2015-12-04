import DataStructures.*;
import Interfaces.IClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class Client extends UnicastRemoteObject implements IClient {
    private View view;

    public Client(View view) throws RemoteException {
        super();
        this.view = view;
    }

    public void setPlayerColor(Player.Color color) {
        view.setPlayerColor(color);
    }

    @Override
    public void updateBoard(BoardChange boardChange) {
        view.updateBoard(boardChange);
    }

    @Override
    public void startGame() {
        view.setWindowSize(500, 500);
        view.startGame();
    }

    @Override
    public void gameOver() {

    }
}
