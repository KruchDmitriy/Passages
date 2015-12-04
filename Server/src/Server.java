import DataStructures.*;
import Exceptions.ServerRemoteException;
import Interfaces.IClient;
import Interfaces.IServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by VladVin on 03.12.2015.
 */
public class Server extends Thread implements IServer {
    private enum Command {NONE, TERMINATE}

    private HashMap<UUID, Room> rooms;
    private HashMap<UUID, Player> players;

    private Command currentCommand = Command.NONE;

    private volatile boolean running = true;

    public Server() {
        this.rooms = new HashMap<>();
        this.players = new HashMap<>();
    }

    @Override
    public void register(String name, UUID playerId, IClient client) throws RemoteException {
        Player player = new Player(name, playerId, client);
        player.start();
        players.put(playerId, player);
    }

    @Override
    public UUID createRoom(String roomName, int size, UUID playerId) throws RemoteException {
        Room room = new Room(roomName, size);
        UUID roomId = UUID.randomUUID();
        rooms.put(roomId, room);
        Player player = players.get(playerId);
        room.joinRoom(new Pair<>(playerId, player));
        return roomId;
    }

    @Override
    public void joinRoom(UUID roomId, UUID playerId) throws RemoteException {
        Player player = players.get(playerId);
        if (player == null) {
            throw new ServerRemoteException(ServerRemoteException.Code.PLAYER_NOT_REGISTERED);
        }
        rooms.get(roomId).joinRoom(new Pair<>(roomId, player));
    }

    @Override
    public void leaveRoom(UUID roomId, UUID playerId) throws RemoteException {
        Room room = rooms.get(roomId);
        room.leaveRoom(playerId);
        if (room.isFree()) {
            rooms.remove(roomId);
        }
    }

    @Override
    public List<RoomInfo> getRooms(UUID playerId) throws RemoteException {
        UUID[] roomIdList = (UUID[])rooms.keySet().toArray();
        Room[] roomList = (Room[])rooms.values().toArray();
        List<RoomInfo> roomsInfo = new ArrayList<>();
        for (int i = 0; i < roomList.length; i++) {
            UUID roomId = roomIdList[i];
            Room room = roomList[i];
            DataStructures.Player bluePlayer = null;
            DataStructures.Player redPlayer = null;
            if (room.getBluePlayerInfo() != null) {
                bluePlayer = new DataStructures.Player(room.getBluePlayerInfo().getRight().getPlayerName(),
                        room.getBluePlayerInfo().getLeft());
            }
            if (room.getRedPlayerInfo() != null) {
                redPlayer = new DataStructures.Player(room.getRedPlayerInfo().getRight().getPlayerName(),
                        room.getRedPlayerInfo().getLeft());
            }
            RoomInfo roomInfo = new RoomInfo(room.getName(), roomId, room.getBoard().getSize(), bluePlayer, redPlayer);
            roomsInfo.add(roomInfo);
        }
        return roomsInfo;
    }

    @Override
    public void takeEdge(BoardChange boardChange, UUID roomId, UUID playerId) throws RemoteException {
        rooms.get(roomId).takeEdge(boardChange);
    }

    @Override
    public void error(String errorMessage) throws RemoteException {

    }

    @Override
    public void run() {
        super.run();

        while (running) {
            try {
                currentCommand.wait();
            } catch (InterruptedException e) {
                break;
            }
            switch (currentCommand) {
                case TERMINATE:
                    return;
            }
        }
    }

    public void terminate() {
        running = false;
        currentCommand = Command.TERMINATE;
        currentCommand.notify();
    }
}
