package Interfaces;

import DataStructures.BoardChange;
import DataStructures.Room;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface IServer extends Remote {
    void register(String name) throws RemoteException;
    void joinRoom(UUID roomId, UUID playerId) throws RemoteException;
    void createRoom(String roomName, int size, UUID playerId) throws RemoteException;
    void leaveRoom(UUID roomId, UUID playerId) throws RemoteException;
    List<Room> getRooms() throws RemoteException;
    void takeEdge(BoardChange boardChange, UUID roomId) throws RemoteException;
    void error(String errorMessage) throws RemoteException;
}
