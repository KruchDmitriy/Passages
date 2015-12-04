import DataStructures.*;
import Interfaces.IClient;

import java.util.List;
import java.util.UUID;

public class Client implements IClient {
    private Player player;
    private Room room;
    private View view;

    public Client(View view) {
        this.view = view;
    }

    public void setPlayerColor(Player.Color color) {
        player.setColor(color);
    }

    @Override
    public void updateRooms(List<RoomInfo> rooms) {
        view.setRooms(rooms);
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
