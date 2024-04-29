// package it.polimi.ingsw.codexnaturalis.network.Socket;

// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.IOException;

// import it.polimi.ingsw.codexnaturalis.view.tui.CliVerifier;

// public class ClientHandler implements VirtualView {
// final CliVerifier controller;
// final Server server;
// final BufferedReader input;
// final VirtualView view;

// public ClientHandler(CliVerifier controller, Server server, BufferedReader
// input, BufferedWriter output) {
// this.controller = controller;
// this.server = server;
// this.input = input;
// this.view = new ClientProxy(output);
// }

// public void runVirtualView() throws IOException {
// String line;
// // Read message type
// while ((line = input.readLine()) != null) {
// // Read message and perform action
// switch (line) {
// case "connect" -> {
// }
// case "add" -> {
// // this.controller.add(Integer.parseInt(input.readLine()));
// // this.server.broadcastUpdate(this.controller.getCurrent());
// }
// case "reset" -> {
// // this.controller.reset();
// // this.server.broadcastUpdate(this.controller.getCurrent());
// }
// default -> System.err.println("[INVALID MESSAGE]");
// }
// }
// }

// @Override
// public void showValue(Integer number) {
// synchronized (this.view) {
// this.view.showValue(number);
// }
// }

// @Override
// public void reportError(String details) {
// synchronized (this.view) {
// this.view.reportError(details);
// }
// }
// }
