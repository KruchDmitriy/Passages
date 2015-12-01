import DataStructures.*;
import Interfaces.IClient;

import java.util.List;
import java.util.UUID;

public class Client implements IClient {
    private Player player;
    private Room room;
    private View view;
    private Connection connection;

    public Client(View view, Connection connection) {
        this.view = view;
        this.connection = connection;
    }

    @Override
    public void setPlayer(Player player) {
        assert player != null;

        if (this.player != null) {
            connection.sendError("Player already set");
        }
        this.player = player;
        view.setPlayer(player);
    }

    public void setPlayerColor(Player.Color color) {
        player.setColor(color);
    }

    @Override
    public void updateRooms(List<Room> rooms) {
        view.setWindowSize(400, 275);
        view.chooseTheRoom(rooms);
    }

    @Override
    public void setRoom(Room room) {
        assert room.getBoard() != null;

        this.room = room;
        view.setRoom(room);
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

    @Override
    public void error(String errorMessage) {
        System.out.println(errorMessage);
    }
}
