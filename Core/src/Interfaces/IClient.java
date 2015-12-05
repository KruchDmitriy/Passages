package Interfaces;

import DataStructures.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface IClient extends Remote {
    void startGame() throws RemoteException;
    void setPlayerColor(Player.Color color) throws RemoteException;
    void updateBoard(BoardChange boardChange) throws RemoteException;
    void isYourTurn(boolean step) throws RemoteException;
    void gameOver() throws RemoteException;
}

