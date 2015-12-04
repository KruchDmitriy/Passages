import Interfaces.IServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    private final static String serverName = "Server";

    public static void main(String[] args) throws Exception {
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }

        IServer server = new Server();
        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        Naming.rebind(serverName, server);
        System.out.println("Server stub bound");
    }
}
