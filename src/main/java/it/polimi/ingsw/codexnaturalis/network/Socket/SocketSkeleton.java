package it.polimi.ingsw.codexnaturalis.network.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualClientHandler;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

public class SocketSkeleton implements VirtualClient, Runnable {

    private VirtualClientHandler server;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public SocketSkeleton (Socket socket) throws IOException {
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    public void setClientHandler (VirtualClientHandler clientHandler){
        this.server = clientHandler;
    }

    // we are on the server
    // we override to avoid the direct call of the client method
    // we propagate the same informations through the stream
    @Override
    public void queueUpdate(Event event) throws IllegalStateException {
        try {
            output.writeObject(event);
            output.flush();
            output.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // reading the commands coming from the actual client and forwarding them to the server
    @Override
    public void run() {
        while (true) {
            try {
                Command command = (Command) input.readObject();
                this.server.updateQueue(command);

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
