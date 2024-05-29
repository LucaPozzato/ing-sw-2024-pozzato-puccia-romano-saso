package it.polimi.ingsw.codexnaturalis;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import it.polimi.ingsw.codexnaturalis.utils.DefaultValue;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        System.out.println("\033[2J\033[1;1H");
        System.setProperty("java.rmi.server.hostname", DefaultValue.serverIP);
        System.setProperty("java.rmi.server.port", Integer.toString(DefaultValue.port_RMI));

        try {
            RmiServer rmiServer = new RmiServer();
            VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(rmiServer, 0);
            Registry registry = LocateRegistry.createRegistry(DefaultValue.port_RMI);
            registry.bind(DefaultValue.servername_RMI, stub);

            SocketServer socketServer = new SocketServer();
            new Thread(socketServer).start();
            rmiServer.run();

            Map<Integer, Game> games = new HashMap<>();
            // Game game = new Game(0, (RmiServer) rmiServer, socketServer);
            rmiServer.setGames(games);
            rmiServer.setSocketServer(socketServer);
            socketServer.setGames(games);
            socketServer.setRmiServer(rmiServer);

        } catch (RemoteException e) {
            System.err.println("Error starting server");
            System.err.println("Exception details:");
            e.printStackTrace(System.err);
        }

        System.out.println("Server started");
    }
}