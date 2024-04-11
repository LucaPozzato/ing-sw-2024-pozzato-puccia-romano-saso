package it.polimi.ingsw.codexnaturalis.network.Socket;

import java.io.BufferedWriter;
import java.io.PrintWriter;

public class ClientProxy implements VirtualView {
    final PrintWriter output;

    public ClientProxy(BufferedWriter output) {
        this.output = new PrintWriter(output);
    }

    @Override
    public void showValue(Integer number) {
        output.println("update");
        output.println(number);
        output.flush();
    }

    @Override
    public void reportError(String details) {
        output.println("error");
        output.println(details);
        output.flush();
    }
}
