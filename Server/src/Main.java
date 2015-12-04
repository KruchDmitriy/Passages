import Interfaces.IServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by VladVin on 04.12.2015.
 */
public class Main {
    private static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        String serverName = "PassagesServer";
        IServer server = new Server();
        try {
            IServer stub = (IServer) UnicastRemoteObject.exportObject(server, 55555);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(serverName, stub);
            System.out.println("Server stub bound");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
