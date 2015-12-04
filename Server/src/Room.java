import DataStructures.Board;
import DataStructures.BoardChange;
import DataStructures.Player.Color;
import DataStructures.Pair;
import DataStructures.Scores;
import Exceptions.ServerRemoteException;

import java.rmi.RemoteException;
import java.util.UUID;

public class Room {
    private String name;

    private Board board;

    private Pair<UUID, Player> bluePlayerInfo;
    private Pair<UUID, Player> redPlayerInfo;
    private Scores scores;

    public Room(String name, int boardSize) {
        this.name = name;
        this.board = new Board(boardSize);
    }

    public synchronized void joinRoom(Pair<UUID, Player> playerInfo) throws ServerRemoteException {
        if (bluePlayerInfo == null) {
            bluePlayerInfo = playerInfo;
        } else if (redPlayerInfo == null) {
            redPlayerInfo = playerInfo;
        } else {
            throw new ServerRemoteException(ServerRemoteException.Code.ROOM_IS_FULL);
        }
        if (bluePlayerInfo != null && redPlayerInfo != null) {
            bluePlayerInfo.getRight().setColor(Color.BLUE);
            redPlayerInfo.getRight().setColor(Color.RED);
            bluePlayerInfo.getRight().startGame();
            redPlayerInfo.getRight().startGame();
        }
    }

    public synchronized void leaveRoom(UUID playerId) {
        if (bluePlayerInfo != null) {
            if (bluePlayerInfo.getLeft().equals(playerId)) {
                bluePlayerInfo = redPlayerInfo;
            }
        } else if (redPlayerInfo != null) {
            if (bluePlayerInfo.getLeft().equals(playerId)) {
                redPlayerInfo = null;
            }
        }
        bluePlayerInfo.getRight().setColor(Color.NONE);
        redPlayerInfo.getRight().setColor(Color.NONE);
    }

    public synchronized void takeEdge(BoardChange boardChange) throws ServerRemoteException {
        if (bluePlayerInfo != null && redPlayerInfo != null) {
            board.apply(boardChange);
            bluePlayerInfo.getRight().updateBoard(boardChange);
            redPlayerInfo.getRight().updateBoard(boardChange);
        } else {
            throw new ServerRemoteException(ServerRemoteException.Code.GAME_NOT_STARTED);
        }
    }

    public synchronized boolean isFree() {
        if (bluePlayerInfo == null && redPlayerInfo == null) {
            return true;
        } else {
            return false;
        }
      }

    public String getRoomName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public Pair<UUID, Player> getBluePlayerInfo() {
        return bluePlayerInfo;
    }

    public Pair<UUID, Player> getRedPlayerInfo() {
        return redPlayerInfo;
    }

    public Scores getScores() {
        return scores;
    }
}
