package it.polimi.ingsw.codexnaturalis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

/**
 * Entry point for the server application. It initializes the RMI and socket
 * servers
 * and binds the necessary objects for remote communication.
 */
public class ServerMain {

    /**
     * Main method to start the server application.
     *
     * @param args command-line arguments (not used)
     * @throws Exception if an error occurs during server startup
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\033[2J\033[1;1H");
        String serverIP = "";

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please insert the server ip address:   ");
        serverIP = input.readLine();
        while (!serverIP.matches(
                "\\b((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\b")) {
            System.err.print("Invalid IP address, please try again:   ");
            serverIP = input.readLine();
        }
        DefaultValue.serverIP = serverIP;

        System.out.println("\033[2J\033[1;1H");

        System.setProperty("java.rmi.server.hostname", DefaultValue.serverIP);
        System.setProperty("java.rmi.server.port", Integer.toString(DefaultValue.port_RMI));

        try {
            RmiServer rmiServer = new RmiServer();
            VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(rmiServer, 0);
            Registry registry = LocateRegistry.createRegistry(DefaultValue.port_RMI);
            registry.rebind(DefaultValue.servername_RMI, stub);

            SocketServer socketServer = new SocketServer();
            new Thread(socketServer).start();
            rmiServer.run();

            Map<Integer, Game> games = new HashMap<>();
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