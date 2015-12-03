package Interfaces;

import DataStructures.BoardChange;
import DataStructures.Player;
import DataStructures.Room;
import DataStructures.Scores;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface IClient extends Remote {
    void setPlayer(Player player) throws RemoteException;
    void updateRooms(List<Room> rooms) throws RemoteException;
    void updateBoard(BoardChange boardChange) throws RemoteException;
    void startGame() throws RemoteException;
    void setRoom(Room room) throws RemoteException;
    void setPlayerColor(Player.Color color) throws RemoteException;
    void gameOver() throws RemoteException;
    public void error(String errorMessage) throws RemoteException;
}
