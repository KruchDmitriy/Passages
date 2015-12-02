import DataStructures.*;
import Interfaces.IServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerStub implements IServer {
    Client client;
    ArrayList<Player> players;
    ArrayList<Room> rooms;

    public ServerStub(Client client) {
        this.client = client;

        players = new ArrayList<>();
        players.add(new Player("Vasya"));
        players.add(new Player("Peter"));
        players.add(new Player("John"));
        players.add(new Player("Piero"));
        players.add(new Player("Phantom"));

        rooms = new ArrayList<>();
        rooms.add(new Room("Fiesta", 3, players.get(0)));
        rooms.add(new Room("Canonball", 3, players.get(1)));
        rooms.add(new Room("Yuppi", 4, players.get(2)));
        rooms.add(new Room("Forever alone", 3, players.get(3)));
        rooms.get(0).setRedPlayer(players.get(4));
    }

    @Override
    public void register(String name) {
        System.out.println("Register player " + name);
        Player player = new Player(name);
        players.add(player);
        client.setPlayer(player);
        client.updateRooms(rooms);
    }

    @Override
    public void joinRoom(UUID roomId, UUID playerId) {
        System.out.println("Join room " + roomId +
                " player with id " + playerId);
        Room room = rooms.stream().
                filter(p -> p.getId() == roomId).findFirst().get();
        Player player = players.stream().
                filter(p -> p.getId() == playerId).findFirst().get();

        if (room.getRedPlayer() == null) {
            room.setRedPlayer(player);
            player.setColor(Player.Color.RED);
            client.setPlayerColor(Player.Color.RED);
        } else if (room.getBluePlayer() == null) {
            room.setBluePlayer(player);
            player.setColor(Player.Color.BLUE);
            client.setPlayerColor(Player.Color.BLUE);
        } else {
            error("The room isn't empty");
            client.updateRooms(rooms);
            return;
        }

        client.setRoom(room);
        client.startGame();
    }

    @Override
    public void createRoom(String roomName, int size, UUID playerId) {
        System.out.println("Create room " + roomName);
        Player player = players.stream().
                filter(p -> p.getId() == playerId).findFirst().get();
        Room room = new Room(roomName, size, player);
        player.setColor(Player.Color.BLUE);
        rooms.add(room);
        client.setRoom(room);
    }

    @Override
    public void leaveRoom(UUID roomId, UUID playerId) {
        System.out.println("Leave room " + roomId +
                " player with id " + playerId);
        Room room = rooms.stream().
                filter(p -> p.getId() == roomId).findFirst().get();
        Player player = players.stream().
                filter(p -> p.getId() == playerId).findFirst().get();
        room.unbindPlayer(player);
        if (room.getRedPlayer() == null &&
                room.getBluePlayer() == null) {
            System.out.println("Remove room " + room.getName());
            rooms.remove(room);
        }
        player.setColor(null);
        client.updateRooms(rooms);
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public void takeEdge(BoardChange boardChange, UUID roomId) {
        System.out.println("Board change in " + roomId + "\n take edge " +
                boardChange.getI() + " " + boardChange.getJ());
        Room room = rooms.stream().
                filter(p -> p.getId() == roomId).findFirst().get();
        Board board = room.getBoard();
        boolean anotherOneTurn = board.apply(boardChange);
        client.updateBoard(boardChange);
    }

    @Override
    public void error(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }
}
