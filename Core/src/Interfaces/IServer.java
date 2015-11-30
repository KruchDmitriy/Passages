package Interfaces;

import DataStructures.BoardChange;
import DataStructures.Room;

import java.rmi.Remote;
import java.util.List;
import java.util.UUID;

public interface IServer extends Remote {
    void register(String name);
    void joinRoom(UUID roomId, UUID playerId);
    void createRoom(String roomName, int size, UUID playerId);
    void leaveRoom(UUID roomId, UUID playerId);
    List<Room> getRooms();
    void takeEdge(BoardChange boardChange, UUID playerId);
    void error(String errorMessage);
}
