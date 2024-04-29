// package it.polimi.ingsw.codexnaturalis.network.Socket;

// import java.io.*;
// import java.net.ServerSocket;
// import java.net.Socket;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.concurrent.BlockingQueue;
// import java.util.concurrent.LinkedBlockingQueue;

// import it.polimi.ingsw.codexnaturalis.view.tui.CliVerifier;

// public class Server {
// final ServerSocket listenSocket;
// final CliVerifier controller;
// final List<ClientHandler> clients = new ArrayList<>();

// public Server(ServerSocket listenSocket, CliVerifier controller) {
// this.listenSocket = listenSocket;
// this.controller = controller;
// }

// private void runServer() throws IOException {
// Socket clientSocket = null;
// while ((clientSocket = this.listenSocket.accept()) != null) {
// InputStreamReader socketRx = new
// InputStreamReader(clientSocket.getInputStream());
// OutputStreamWriter socketTx = new
// OutputStreamWriter(clientSocket.getOutputStream());

// ClientHandler handler = new ClientHandler(this.controller, this, new
// BufferedReader(socketRx),
// new BufferedWriter(socketTx));

// clients.add(handler);
// new Thread(() -> {
// try {
// handler.runVirtualView();
// } catch (IOException e) {
// throw new RuntimeException(e);
// }
// }).start();
// }
// }

// public void broadcastUpdate(Integer value) {
// synchronized (this.clients) {
// for (var client : this.clients) {
// client.showValue(value);
// }
// }
// }

// public static void main(String[] args) throws IOException {
// String host = args[0];
// int port = Integer.parseInt(args[1]);

// ServerSocket listenSocket = new ServerSocket(port);
// // Perchè nel costruttore del server controller viene passato lo state?
// // se viene creato ad inizio game e terminato alla fine basta inserire primo
// // stato del gioco direttamente nella costruzione
// // + non credo che abbiamo informazione sullo stato da qui prima di creare il
// // controller
// // stesso problema in server RMI
// new Server(listenSocket, new CliVerifier());
// }
// }