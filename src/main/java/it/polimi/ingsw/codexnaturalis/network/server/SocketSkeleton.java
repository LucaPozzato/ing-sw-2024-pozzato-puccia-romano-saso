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
import it.polimi.ingsw.codexnaturalis.network.events.Event;

public class SocketSkeleton implements VirtualClient, Runnable {

    private String clientId;
    private String password;
    private final VirtualServer server;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public SocketSkeleton(VirtualServer server, Socket socket) throws IOException {
        this.clientId = null;
        this.password = null;
        this.server = server;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
    }

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

    public void sendCommand(Command command) throws RemoteException {
        this.server.receiveCommand(command);
    }

    public void stop() {
        Thread.currentThread().interrupt();
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ((SocketServer) server).getConnected().put(this, true);
                Command command = (Command) input.readObject();
                if (command != null) {
                    System.out.println("skeleton received command: " + command.getClass().getSimpleName());
                    if ((command instanceof CreateGameCommand) || (command instanceof JoinGameCommand)) {
                        this.clientId = command.getClientId();
                        this.password = command.getPassword(); // password setting socket
                        System.out.println("skeleton client id: " + this.clientId);
                    }
                    sendCommand(command);
                }
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected");
            ((SocketServer) server).getConnected().put(this, false);
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

    @Override
    public String getPassword(){
        return this.password;
    }
    @Override
    public void setPassword(String password){
        this.password = password;
    }
}
