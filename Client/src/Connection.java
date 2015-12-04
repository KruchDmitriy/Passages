import DataStructures.BoardChange;
import DataStructures.Room;
import DataStructures.RoomInfo;
import Exceptions.ServerRemoteException;
import Interfaces.IClient;
import Interfaces.IServer;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class Connection {
    public enum ConnectionType { DEBUG, RELEASE }
    private IServer server;
    private IClient client;

    private ConnectionType type;
    public Connection(ConnectionType type) {
        this.type = type;
        start();
    }

    private void start() {
        if (type == ConnectionType.RELEASE) {
            try {
                server = (IServer) Naming.lookup("Server");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void register(String name, UUID playerId) {
        try {
            Naming.rebind("Client/" + playerId, client);
            server.register(name, playerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRoom(String roomName, int size, UUID playerId) {
        try {
            server.createRoom(roomName, size, playerId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void joinRoom(UUID roomId, UUID playerId) {
        try {
            server.joinRoom(roomId, playerId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<RoomInfo> getRooms() throws Exception {
        try {
            return server.getRooms();
        } catch (ServerRemoteException e) {
            throw new Exception("Cannot update room list");
        }
    }

    public void takeEdge(BoardChange boardChange, UUID roomId) {
        try {
            server.takeEdge(boardChange, roomId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
