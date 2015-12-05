import DataStructures.*;
import DataStructures.Player.Color;
import Exceptions.ServerRemoteException;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room {
    Logger logger = Logger.getLogger(Room.class.getName());

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
            bluePlayerInfo.getRight().isYourTurn(true);
            redPlayerInfo.getRight().isYourTurn(false);
            logger.log(Level.INFO, "Game started");
        }
    }

    public synchronized void leaveRoom(UUID playerId) {
        if (bluePlayerInfo != null) {
            bluePlayerInfo.getRight().setColor(Color.NONE);
        }
        if (redPlayerInfo != null) {
            redPlayerInfo.getRight().setColor(Color.NONE);
        }
        if (bluePlayerInfo != null) {
            if (bluePlayerInfo.getLeft().equals(playerId)) {
                bluePlayerInfo = redPlayerInfo;
                redPlayerInfo = null;
            }
        } else if (redPlayerInfo != null) {
            if (redPlayerInfo.getLeft().equals(playerId)) {
                redPlayerInfo = null;
            }
        }
    }

    public synchronized void takeEdge(BoardChange boardChange) throws ServerRemoteException {
        if (bluePlayerInfo != null && redPlayerInfo != null) {
            try {
                boolean myStep = board.apply(boardChange);
                bluePlayerInfo.getRight().updateBoard(boardChange);
                redPlayerInfo.getRight().updateBoard(boardChange);
                if (boardChange.getReservedBy() == Edge.WHO.BLUE) {
                    bluePlayerInfo.getRight().isYourTurn(myStep);
                    redPlayerInfo.getRight().isYourTurn(!myStep);
                } else {
                    bluePlayerInfo.getRight().isYourTurn(!myStep);
                    redPlayerInfo.getRight().isYourTurn(myStep);
                }
            } catch (IllegalStateException e) {

            }
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
