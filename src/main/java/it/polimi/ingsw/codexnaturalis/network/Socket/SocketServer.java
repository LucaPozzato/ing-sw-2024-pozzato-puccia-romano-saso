//package it.polimi.ingsw.codexnaturalis.network.Socket;
//
////import java.io.IOException;
////import java.io.ObjectInputStream;
////import java.io.ObjectOutputStream;
////import java.net.Socket;
////import java.rmi.RemoteException;
//
//
//import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
////TODO choose wether to make the VirtualClient common both to socket and RMI
//import it.polimi.ingsw.codexnaturalis.controller.InitState;
//import it.polimi.ingsw.codexnaturalis.model.game.Game;
//import it.polimi.ingsw.codexnaturalis.network.RMI.Server;
//import it.polimi.ingsw.codexnaturalis.network.RMI.VirtualClient;
//import it.polimi.ingsw.codexnaturalis.network.RMI.VirtualServer;
//import it.polimi.ingsw.codexnaturalis.network.events.Event;
//
//
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.rmi.server.UnicastRemoteObject;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.Queue;
//
//public class SocketServer implements VirtualServer, Runnable {
//
//    final ControllerState controller;
//    final List<VirtualClient> clients = new ArrayList<>();
//    private Queue<Event> eventQueue;
//    final ServerSocket listenSocket;
//
//
//    public SocketServer (ServerSocket listenSocket, ControllerState controller) {
//        //this.listenSocket = listenSocket;
//        this.controller = controller;
//        this.eventQueue = new LinkedList<Event>();
//
//    }
//
//    // TODO choose how to handle Server parameters (host,port)
//    // could use constants
//    public static void main(String[] args) throws IOException {
//
//        try {
//            String host = args[0];
//            int port = Integer.parseInt(args[1]);
//            ServerSocket listenSocket = new ServerSocket(port);
//            VirtualServer server = new SocketServer( listenSocket, new InitState(new Game(0)) {
//            });
//            System.out.println("Server pronto...");
//
//        } catch (Exception e) {
//            System.err.println("Server exception: " + e.toString());
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//
//    private void runServer() throws IOException {
//        Socket clientSocket = null;
//        while ((clientSocket = this.listenSocket.accept()) != null) {
//            InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
//            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());
//
//            VirtualClient client = new SocketClient(new BufferedReader(socketRx),
//                    new BufferedWriter(socketTx));
//
//            clients.add(client);
//            new Thread(() -> {
//                try {
//                    //client.runVirtualView();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }).start();
//        }
//    }
//
//    public void queueUpdate(Event event) throws IllegalStateException {
//        eventQueue.add(event);
//        // notify del thread;
//        // gestione exception?
//    }
//
//    public void processEventThread() {
//        new Thread(this::processEvent).start();
//    }
//
//    public void processEvent() {
//        // TODO: gestione eccezioni
//        while (true) {
//            try {
//                Event event = this.eventQueue.poll();
//                synchronized (this.controller) {
//                    event.run(controller);
//                }
//            } catch (Exception e) {
//                // System.err.println("");
//                break;
//            }
//        }
//    }
//
//    public void broadcastUpdate(UpdateEvent updateEvent) {
//        synchronized (this.clients) {
//            for (var client : this.clients) {
//                client.updateClient(updateEvent);
//            }
//        }
//    }
//
//}
//
//

