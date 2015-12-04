import DataStructures.*;
import Exceptions.ServerRemoteException;
import Interfaces.IClient;
import Interfaces.IServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends UnicastRemoteObject implements IServer {
    // All operations on the server are synchronized
    Logger logger = Logger.getLogger(Server.class.getName());

    private ConcurrentHashMap<UUID, Room> rooms;
    private ConcurrentHashMap<UUID, Player> players;

    public Server() throws RemoteException {
        super();
        this.rooms = new ConcurrentHashMap<>();
        this.players = new ConcurrentHashMap<>();
        logger.log(Level.INFO, "Server has been started");
    }

    @Override
    public void register(String name, UUID playerId) throws RemoteException {
        Player player = new Player(name, playerId);
        players.put(playerId, player);
        logger.log(Level.INFO, "New player has been registered");
    }

    @Override
    public UUID createRoom(String roomName, int size, UUID playerId) throws RemoteException {
        Room room = new Room(roomName, size);
        UUID roomId = UUID.randomUUID();
        rooms.put(roomId, room);
        Player player = players.get(playerId);
        room.joinRoom(new Pair<>(playerId, player));
        logger.log(Level.INFO, "New room has been created");
        return roomId;
    }

    @Override
    public void joinRoom(UUID roomId, UUID playerId) throws RemoteException {
        Player player = players.get(playerId);
        if (player == null) {
            throw new ServerRemoteException(ServerRemoteException.Code.PLAYER_NOT_REGISTERED);
        }
        rooms.get(roomId).joinRoom(new Pair<>(roomId, player));
        logger.log(Level.INFO, "Player has been joined to the room");
//        updateRooms();
    }

    @Override
    public synchronized void leaveRoom(UUID roomId, UUID playerId) throws RemoteException {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new ServerRemoteException(ServerRemoteException.Code.ROOM_NOT_CREATED);
        }
        room.leaveRoom(playerId);
        logger.log(Level.INFO, "Player has been leaved the room");
        if (room.isFree()) {
            rooms.remove(roomId);
            logger.log(Level.INFO, "Room has been deleted");
        }
//        updateRooms();
    }

    @Override
    public void takeEdge(BoardChange boardChange, UUID roomId) throws RemoteException {
        rooms.get(roomId).takeEdge(boardChange);
        logger.log(Level.INFO, "Board change has been applied");
    }

    private void updateRooms() {
        List<RoomInfo> roomInfoList = getRooms();
        Iterator<Player> it = players.values().iterator();
        while (it.hasNext()) {
            Player player = it.next();
//            player.updateRooms(roomInfoList);
        }
        logger.log(Level.INFO, "Room list has been broadcast");
    }

    @Override
    public List<RoomInfo> getRooms() {
        List<RoomInfo> roomsInfo = new ArrayList<>();
//        UUID[] roomIdList = (UUID[])(rooms.keys().toArray());
//        Room[] roomList = (Room[])(rooms.values().toArray());
//        while (rooms.keys().hasMoreElements()) {
//            UUID roomId = rooms.keys().nextElement();
//            Room room = rooms.values().iterator().next();
//            rooms.values().iterator().hasNext();
//            DataStructures.Player bluePlayer = null;
//            DataStructures.Player redPlayer = null;
//            if (room.getBluePlayerInfo() != null) {
//                bluePlayer = new DataStructures.Player(room.getBluePlayerInfo().getRight().getPlayerName(),
//                        room.getBluePlayerInfo().getLeft());
//            }
//            if (room.getRedPlayerInfo() != null) {
//                redPlayer = new DataStructures.Player(room.getRedPlayerInfo().getRight().getPlayerName(),
//                        room.getRedPlayerInfo().getLeft());
//            }
//            RoomInfo roomInfo = new RoomInfo(room.getRoomName(), roomId, room.getBoard().getSize(), bluePlayer, redPlayer);
//            roomsInfo.add(roomInfo);
//        }

        for (int i = 0; i < rooms.keySet().toArray().length; i++) {
            UUID roomId = (UUID) rooms.keySet().toArray()[i];
            Room room = (Room) rooms.values().toArray()[i];
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
            RoomInfo roomInfo = new RoomInfo(room.getRoomName(), roomId, room.getBoard().getSize(), bluePlayer, redPlayer);
            roomsInfo.add(roomInfo);
        }
        logger.log(Level.INFO, "Room list has been sent");
        return roomsInfo;
    }
}
