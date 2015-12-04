import DataStructures.Board;
import DataStructures.BoardChange;
import DataStructures.RoomInfo;
import Exceptions.ServerRemoteException;
import Interfaces.IClient;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.UUID;

public class Player{

    private final static String clientName = "Client/";

    private String playerName;
    private UUID id;
    private DataStructures.Player.Color color;

    private IClient client;

    public Player(String name, UUID id) throws ServerRemoteException {
        this.playerName = name;
        this.id = id;
        try {
            this.client = (IClient) Naming.lookup(clientName + id);
        } catch (Exception e) {
            throw new ServerRemoteException(ServerRemoteException.Code.PLAYER_NOT_REGISTERED);
        }
        this.color = DataStructures.Player.Color.NONE;
    }

    public String getPlayerName() {
        return playerName;
    }

    public DataStructures.Player.Color getColor() {
        return color;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setColor(DataStructures.Player.Color color) {
        this.color = color;
        try {
            client.setPlayerColor(color);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        try {
            client.startGame();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateBoard(BoardChange boardChange) {
        try {
            client.updateBoard(boardChange);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
//    public void updateRooms(List<RoomInfo> roomInfoList) {
//        try {
//            client.updateRooms(roomInfoList);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
}
