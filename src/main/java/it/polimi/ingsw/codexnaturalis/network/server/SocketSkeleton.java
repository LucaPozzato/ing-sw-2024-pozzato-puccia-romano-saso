package it.polimi.ingsw.codexnaturalis.network.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.commands.CreateGameCommand;
import it.polimi.ingsw.codexnaturalis.network.commands.JoinGameCommand;
import it.polimi.ingsw.codexnaturalis.network.commands.Ping;
import it.polimi.ingsw.codexnaturalis.network.commands.RejoinGameCommand;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

public class SocketSkeleton implements VirtualClient, Runnable {

    private String clientId;
    private final VirtualServer server;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public SocketSkeleton(VirtualServer server, Socket socket) throws IOException {
        this.clientId = null;
        this.server = server;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Sends an Event to the client over the socket connection
     *
     * @param event The Event to be sent
     * @throws RemoteException If there is a communication-related exception
     */
    @Override
    public void receiveEvent(Event event) throws RemoteException {
        try {
            output.writeObject(event);
            output.flush();
            output.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a Command to the server
     *
     * @param command The Command to be sent to the server
     * @throws RemoteException If there is a communication-related exception
     */
    public void sendCommand(Command command) throws RemoteException {
        this.server.receiveCommand(command);
    }

    /**
     * Stops the execution of the SocketSkeleton instance by interrupting its thread
     * and closing input stream.
     */
    public void stop() {
        Thread.currentThread().interrupt();
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Continuously listens for incoming Commands from the client,
     * processes them, and forwards them to the server
     * Handles special Commands like CreateGameCommand, JoinGameCommand,
     * RejoinGameCommand, and Ping
     * to manage client identification.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Command command = (Command) input.readObject();
                if (command != null) {
                    if (!(command instanceof Ping))
                        System.out.println("skeleton received command: " + command.getClass().getSimpleName());
                    if ((command instanceof CreateGameCommand) || (command instanceof JoinGameCommand)
                            || command instanceof RejoinGameCommand || command instanceof Ping) {
                        this.clientId = command.getClientId();
                    }
                    sendCommand(command);
                }
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected EOF");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public void ping() throws RemoteException {
        // do nothing
    }

}
