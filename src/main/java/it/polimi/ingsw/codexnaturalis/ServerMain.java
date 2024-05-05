package it.polimi.ingsw.codexnaturalis;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.controller.InitState;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import it.polimi.ingsw.codexnaturalis.utils.DefaultValue;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public static void main(String[] args) {
        System.out.println("\033[2J\033[1;1H");

        ControllerState controller = new InitState(new Game(0));

        try {

            RmiServer rmiServer = new RmiServer(controller);
            Registry registry = LocateRegistry.createRegistry(DefaultValue.port_RMI);
            registry.rebind(DefaultValue.servername_RMI, rmiServer);

            SocketServer socketServer = new SocketServer(controller);
            new Thread(socketServer).start();

        } catch (RemoteException e) {
            System.err.println("Error starting server");
            System.err.println("Exception details:");
            e.printStackTrace(System.err);
        }

        System.out.println("Server started");
    }
}