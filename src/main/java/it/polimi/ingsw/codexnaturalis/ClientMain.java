package it.polimi.ingsw.codexnaturalis;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.client.RmiClient;
import it.polimi.ingsw.codexnaturalis.network.client.SocketClient;
import it.polimi.ingsw.codexnaturalis.utils.DefaultValue;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {

    public static void main(String[] args) throws IOException {

        final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean isRmi = false;
        boolean isCli = false;
        boolean flag = false;
        String line;

        System.out.print("Please insert the type of user interface you would like to play with [cli/gui]:   ");
        line = input.readLine();

        while (!flag) {
            switch (line) {
                case "cli":
                    isCli = true;
                    flag = true;
                    break;
                case "gui":
                    isCli = false;
                    flag = true;
                    break;
                default:
                    System.err.println("We didn't quite catch that, please try again:");
                    line = input.readLine();
            }
        }

        flag = false;

        System.out.print("Please insert the type of connection you would like to use [rmi/socket]:   ");
        line = input.readLine();

        while (!flag) {
            switch (line) {
                case "rmi":
                    isRmi = true;
                    flag = true;
                    break;
                case "socket":
                    isRmi = false;
                    flag = true;
                    break;
                default:
                    System.err.println("We didn't quite catch that, please try again:");
                    line = input.readLine();
            }
        }

        // TODO: I believe we can force the serverip and port as default values.
        // In the case we want to take it as an input from the client,
        // the code below has to be implemented with a switch that resolves the null
        // choice,
        // which will insert the default values
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

        try {
            input.close();
        } catch (IOException e) {
            System.err.println("Something went wrong while closing the input: " + e.getMessage());
        }
        // BUG: non chiudere input altrimenti la tui non puo' piu' leggere da stdin

        if (isRmi) {
            VirtualServer server = null;
            Registry registry;
            try {
                registry = LocateRegistry.getRegistry(DefaultValue.Remote_ip, DefaultValue.port_RMI);
                server = (VirtualServer) registry.lookup(DefaultValue.servername_RMI);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
            RmiClient client = new RmiClient(server, isCli);
            client.run();

        } else {
            Socket socket = new Socket(DefaultValue.serverIp, DefaultValue.port_Socket);
            SocketClient client = new SocketClient(socket, isCli);
            client.run();
        }

    }
}
