package it.polimi.ingsw.codexnaturalis.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

public class SocketSkeleton implements VirtualClient, Runnable {

    private final VirtualServer server;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public SocketSkeleton (VirtualServer server, Socket socket) throws IOException {
        this.server = server;
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void receiveEvent(Event event) throws IllegalStateException {
        try {
            output.writeObject(event);
            output.flush();
            output.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(Command command) throws IllegalStateException {
        this.server.receiveCommand(command);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Command command = (Command) input.readObject();
                sendCommand(command);

            } catch (IOException | ClassNotFoundException e) {
                try {
                    input.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

}
