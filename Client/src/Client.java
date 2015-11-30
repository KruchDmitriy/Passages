import DataStructures.*;
import Interfaces.IClient;

import java.util.List;
import java.util.UUID;

public class Client implements IClient {
    private Player player;
    private List<Room> rooms;
    private Board board;
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
    }

    @Override
    public void updateRooms(List<Room> rooms) {
        this.rooms = rooms;
        view.chooseTheRoom(rooms);
    }

    @Override
    public void updateBoard(BoardChange boardChange, Scores scores, UUID playerId, boolean yourTurn) {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void gameOver(Scores scores) {

    }

    @Override
    public void error(String errorMessage) throws Exception {
        throw new Exception(errorMessage);
    }
}
