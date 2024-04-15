package it.polimi.ingsw.codexnaturalis.network.Socket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueExample {
    private final BlockingQueue<Integer> updateQueue = new LinkedBlockingQueue<>();
    private final List<VirtualView> ClientList = new ArrayList<>();

    public void addClient(VirtualView client) {
        synchronized (this.ClientList) {
            this.ClientList.add(client);
        }
    }

    public void queueUpdate(Integer value) throws InterruptedException {
        this.updateQueue.put(value);
    }

    public void startUpdateThread() {
        new Thread(this::runBroadcastLoop).start();
    }

    public void runBroadcastLoop() {
        while (true) {
            try {
                Integer update = this.updateQueue.take();
                synchronized (this.ClientList) {
                    for (var client : this.ClientList) {
                        client.showValue(update);
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("channel closed");
                break;
            }
        }
    }
}
