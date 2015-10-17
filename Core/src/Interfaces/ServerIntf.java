package Interfaces;

import DataStructures.BoardChange;
import DataStructures.Room;

import java.rmi.Remote;
import java.util.List;
import java.util.UUID;

/**
 * Created by VladVin on 17.10.2015.
 */
public interface ServerIntf extends Remote {
    UUID registerMe(String name);
    void joinRoom(UUID roomId, UUID playerId);
    UUID createRoom(String roomName, int nsize, UUID playerId);
    void leaveRoom(UUID roomId, UUID playerId);
    List<Room> getRooms();
    void takeEdge(BoardChange boardChange, UUID playerId);
}
