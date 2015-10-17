package Interfaces;

import DataStructures.BoardChange;
import DataStructures.Room;
import DataStructures.Scores;

import java.rmi.Remote;
import java.util.List;
import java.util.UUID;

/**
 * Created by VladVin on 17.10.2015.
 */
public interface ClientIntf extends Remote {
    void updateRooms(List<Room> rooms);
    void updateBoard(BoardChange boardChange, UUID playerId);
    void startGame(Scores scores);
    void gameOver(Scores scores);
}
