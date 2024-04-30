package it.polimi.ingsw.codexnaturalis.network.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.VirtualClientHandler;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

public class SocketStub implements VirtualServer, VirtualClientHandler, Runnable {

    private VirtualClient client;
    private ObjectInputStream input;

    private ObjectOutputStream output;


    // this override avoids the direct call of the server method
    // in order to forward the command through the TCP Protocol
    @Override
    public void queueUpdate(Command command) {
        try {
            output.writeObject(command);
            output.flush();
            output.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQueue(Command command) {

    }


    // MMM copiato quasi pari da un progetto, mi piace molto though non saprei
    // super coatto, fa la registrazione del client restituendo al posto del client handler questo stub
    // si potrebbe evitare di implementare client handler anche se chiaramente deve fare override dei suoi metodi
    @Override
    public void/*VirtualClientHandler*/ connect(VirtualClient client) throws RemoteException {
        this.client = client;
        //TODO: manage IP and Port
//        try {
//            Socket socket = new Socket(serverIpAddress, serverPort);
//            this.input = new ObjectInputStream(socket.getInputStream());
//            this.output = new ObjectOutputStream(socket.getOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        new Thread(this).start();

        //return this;
    }

    // anche qui preso spunto dall'altro progetto
    // TODO: studiare best practice di chiusura canale e gestione eccezioni
    @Override
    public void run() {
        while (true) {
            try {
                Event event = (Event) input.readObject();
                this.client.queueUpdate(event);
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
