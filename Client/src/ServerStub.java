import DataStructures.BoardChange;
import DataStructures.Player;
import DataStructures.Room;
import Interfaces.IServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerStub implements IServer {
    Client client;

    public ServerStub(Client client) {
        this.client = client;
    }

    @Override
    public void register(String name) {
        System.out.println("Register player " + name);
        Player player = new Player(name);
        client.setPlayer(player);

        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room("Fiesta", 10, new Player("Vasya")));
        rooms.add(new Room("Canonball", 10, new Player("Peter")));
        rooms.add(new Room("Yuppi", 10, new Player("John")));
        rooms.add(new Room("Forever alone", 10, new Player("Piero")));
        client.updateRooms(rooms);
    }

    @Override
    public void joinRoom(UUID roomId, UUID playerId) {

    }

    @Override
    public void createRoom(String roomName, int size, UUID playerId) {
    }

    @Override
    public void leaveRoom(UUID roomId, UUID playerId) {

    }

    @Override
    public List<Room> getRooms() {
        return null;
    }

    @Override
    public void takeEdge(BoardChange boardChange, UUID playerId) {

    }

    @Override
    public void error(String errorMessage) {

    }
}
