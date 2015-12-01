import DataStructures.Room;
import Interfaces.IClient;
import Interfaces.IServer;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class Connection {
    public enum ConnectionType { DEBUG, RELEASE;}
    private IServer server;

    private ConnectionType type;
    public Connection(ConnectionType type) {
        this.type = type;
        start();
    }

    public void setView(View view) {
        setClient(view);
    }

    private void start() {
        if (type == ConnectionType.RELEASE) {
            try {
                Registry registry = LocateRegistry.getRegistry(null, 55555);
                server = (IServer) registry.lookup("IServer");
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        } else {
            // TODO
        }
    }

    private void setClient(View view) {
        Client client = new Client(view, this);

        if (type == ConnectionType.RELEASE) {
            IClient stub;
            try {
                stub = (IClient) UnicastRemoteObject.exportObject(client, 0);
                Registry registry = LocateRegistry.createRegistry(66666);
                registry.bind("IClient", stub);
            } catch (RemoteException | AlreadyBoundException e) {
                e.printStackTrace();
            }
        } else {
            server = new ServerStub(client);
        }
    }

    public void register(String name) {
        server.register(name);
    }

    public void createRoom(String roomName, int size, UUID playerId) {
        server.createRoom(roomName, size, playerId);
    }

    public void joinRoom(UUID roomId, UUID playerId) {
        server.joinRoom(roomId, playerId);
    }

    public List<Room> getRooms() {
        return server.getRooms();
    }

    public void sendError(String errorMessage) {
        server.error(errorMessage);
    }
}
