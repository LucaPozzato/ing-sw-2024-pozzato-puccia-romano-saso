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

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("\033[2J\033[1;1H");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        try {

            boolean isCli = chooseUserInterface(input);
            boolean isRmi = chooseConnectionType(input);

            startClient(isCli, isRmi);

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
                    // si potrebbe aggiungere un numero limite di tentativi
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
                    // si potrebbe aggiungere un numero limite di tentativi
                    System.err.println("We didn't quite catch that, please try again:");
                    line = input.readLine();
            }
        }
    }

    private static void startClient(boolean isCli, boolean isRmi) {
        if (isRmi) {
            startRmiClient(isCli);
        } else {
            startSocketClient(isCli);
        }
    }

    private static void startRmiClient(boolean isCli) {
        try {
            VirtualServer server = null;
            Registry registry;
            registry = LocateRegistry.getRegistry(DefaultValue.remoteIP, DefaultValue.port_RMI);
            server = (VirtualServer) registry.lookup(DefaultValue.servername_RMI);
            RmiClient client = new RmiClient(server, isCli);
            System.out.println(
                    "Client started successfully via RMI connection and " + (isCli ? "CLI" : "GUI") + " interface.");
            client.run();
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private static void startSocketClient(boolean isCli) {
        Socket socket = null;
        try {
            socket = new Socket(DefaultValue.serverIP, DefaultValue.port_Socket);
            SocketClient client = new SocketClient(socket, isCli);
            new Thread(client).start();

            System.out.println(
                    "Client started successfully via socket connection and " + (isCli ? "CLI" : "GUI") + " interface.");
        } catch (IOException e) {
            System.err.println("Error while starting socket client: " + e.getMessage());
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.err.println("Error while closing socket: " + ex.getMessage());
                }
            }
        }
    }
}

// // TODO: I believe we can force the serverip and port as default values.
// // In the case we want to take it as an input from the client,
// // the code below has to be implemented with a switch that resolves the null
// choice,
// // which will insert the default values
// System.out.print("Please insert the server ip address [recommended ...]: ");
// try {
// String inputString = input.readLine();
// serverIpAddress = Integer.parseInt(inputString);
// System.out.println("Serve Ip Address: " + serverIpAddress);
// } catch (IOException e) {
// System.err.println("Something went wrong while reading the input: " +
// e.getMessage());
// } catch (NumberFormatException e) {
// System.err.println("Invalid input. Please be sure to insert a number.");
// }
//
// try {
// input.close();
// } catch (IOException e) {
// System.err.println("Something went wrong while closing the input: " +
// e.getMessage());
// }