package it.polimi.ingsw.codexnaturalis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.client.RmiClient;
import it.polimi.ingsw.codexnaturalis.network.client.SocketClient;
import it.polimi.ingsw.codexnaturalis.utils.DefaultValue;
import javafx.application.Platform;

public class ClientMain {
    public static void main(String[] args) {

        Platform.startup(() -> {
        });

        System.out.println("\033[2J\033[1;1H");
        String[] cmd = { "/bin/sh", "-c", "stty sane </dev/tty" };

        try {
            if (!System.getProperty("os.name").contains("Windows"))
                Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        boolean started = false;

        while (!started)
            try {

                boolean isCli = chooseUserInterface(input);
                boolean isRmi = chooseConnectionType(input);

                started = startClient(isCli, isRmi);

            } catch (IOException e) {
                System.err.println("An error occurred while reading input: " + e.getMessage());
            }
    }

    private static boolean chooseUserInterface(BufferedReader input) throws IOException {

        System.out.print("Please insert the type of user interface you would like to play with [cli/gui]:   ");
        String line = input.readLine();

        while (true) {
            switch (line) {
                case "cli":
                    return true;
                case "gui":
                    return false;
                default:
                    System.err.println("We didn't quite catch that, please try again:");
                    line = input.readLine();
            }
        }
    }

    private static boolean chooseConnectionType(BufferedReader input) throws IOException {

        System.out.print("Please insert the type of connection you would like to use [rmi/socket]:   ");
        String line = input.readLine();

        while (true) {
            switch (line) {
                case "rmi":
                    return true;
                case "socket":
                    return false;
                default:
                    System.err.println("We didn't quite catch that, please try again:");
                    line = input.readLine();
            }
        }
    }

    private static Boolean startClient(boolean isCli, boolean isRmi) {
        if (isRmi) {
            return startRmiClient(isCli);
        } else {
            return startSocketClient(isCli);
        }
    }

    private static Boolean startRmiClient(boolean isCli) {
        try {
            VirtualServer server = null;
            Registry registry;
            registry = LocateRegistry.getRegistry(DefaultValue.remoteIP, DefaultValue.port_RMI);
            server = (VirtualServer) registry.lookup(DefaultValue.servername_RMI);
            RmiClient client = new RmiClient(server, isCli);
            System.out.println(
                    "Client started successfully via RMI connection and " + (isCli ? "CLI" : "GUI") + " interface.");
            client.run();
            return true;
        } catch (RemoteException e) {
            System.err.println("Server not started yet, please try again");
            return false;
        } catch (NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Boolean startSocketClient(boolean isCli) {
        Socket socket = null;
        try {
            socket = new Socket(DefaultValue.serverIP, DefaultValue.port_Socket);
            SocketClient client = new SocketClient(socket, isCli);
            new Thread(client).start();

            System.out.println(
                    "Client started successfully via socket connection and " + (isCli ? "CLI" : "GUI") + " interface.");
            return true;
        } catch (IOException e) {
            System.err.println("Server not started yet, please try again");
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.err.println("Error while closing socket: " + ex.getMessage());
                }
            }
            return false;
        }
    }

}
