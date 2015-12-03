package Interfaces;

import DataStructures.BoardChange;
import DataStructures.Player;
import DataStructures.Room;
import DataStructures.RoomInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface IServer extends Remote {
    void register(String name, UUID playerId, IClient client) throws RemoteException;
    void joinRoom(UUID roomId, UUID playerId) throws RemoteException;
    UUID createRoom(String roomName, int size, UUID playerId) throws RemoteException;
    void leaveRoom(UUID roomId, UUID playerId) throws RemoteException;
    List<RoomInfo> getRooms(UUID playerId) throws RemoteException;
    void takeEdge(BoardChange boardChange, UUID roomId, UUID playerId) throws RemoteException;
    void error(String errorMessage) throws RemoteException;
}
