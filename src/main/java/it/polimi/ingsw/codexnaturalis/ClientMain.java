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

/**
 * Entry point for the client application. It handles the user interface
 * selection and the connection type selection, hence starts the client
 * accordingly.
 */

public class ClientMain {

    /**
     * Main method to start the client application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        boolean started = false;

        while (!started)
            try {
                String serverIP = "";
                System.out.print("Please insert the server ip address:   ");
                serverIP = input.readLine();

                while (!serverIP.matches(
                        "\\b((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\b")) {
                    System.err.print("Invalid IP address, please try again:   ");
                    serverIP = input.readLine();
                }

                DefaultValue.serverIP = serverIP;

                boolean isCli = chooseUserInterface(input);
                boolean isRmi = chooseConnectionType(input);

                started = startClient(isCli, isRmi);

            } catch (IOException e) {
                System.err.println("An error occurred while reading input: " + e.getMessage());
            }
    }

    /**
     * Prompts the user to choose the type of user interface.
     *
     * @param input BufferedReader to read user input
     * @return true if CLI is chosen, false if GUI is chosen
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Prompts the user to choose the type of connection.
     *
     * @param input BufferedReader to read user input
     * @return true if RMI is chosen, false if socket is chosen
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Starts the client with the specified user interface and connection type.
     *
     * @param isCli true if CLI is chosen, false if GUI is chosen
     * @param isRmi true if RMI is chosen, false if socket is chosen
     * @return true if the client started successfully, false otherwise
     */
    private static Boolean startClient(boolean isCli, boolean isRmi) {
        if (isCli) {
            System.out.println("Starting CLI client...");
        } else {
            System.out.println("Starting GUI client...");
            Platform.startup(() -> {
            });
        }
        if (isRmi) {
            return startRmiClient(isCli);
        } else {
            return startSocketClient(isCli);
        }
    }

    /**
     * Starts the client using RMI.
     *
     * @param isCli true if CLI is chosen, false if GUI is chosen
     * @return true if the client started successfully, false otherwise
     */
    private static Boolean startRmiClient(boolean isCli) {
        try {
            VirtualServer server = null;
            Registry registry;
            registry = LocateRegistry.getRegistry(DefaultValue.serverIP, DefaultValue.port_RMI);
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

    /**
     * Starts the client using a socket connection.
     *
     * @param isCli true if CLI is chosen, false if GUI is chosen
     * @return true if the client started successfully, false otherwise
     */

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
