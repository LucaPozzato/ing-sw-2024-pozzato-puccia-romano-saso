package it.polimi.ingsw.codexnaturalis;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import it.polimi.ingsw.codexnaturalis.utils.DefaultValue;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        System.out.println("\033[2J\033[1;1H");

        try {
            RmiServer rmiServer = new RmiServer();
            VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(rmiServer, 0);
            Registry registry = LocateRegistry.createRegistry(DefaultValue.port_RMI);
            registry.rebind(DefaultValue.servername_RMI, stub);

            SocketServer socketServer = new SocketServer();
            new Thread(socketServer).start();
            rmiServer.run();

            Game game = new Game(0, (RmiServer) rmiServer, socketServer);
            ControllerState controller = game.getState();
            rmiServer.setController(controller);
            socketServer.setController(controller);

        } catch (RemoteException e) {
            System.err.println("Error starting server");
            System.err.println("Exception details:");
            e.printStackTrace(System.err);
        }

        System.out.println("Server started");
    }
}