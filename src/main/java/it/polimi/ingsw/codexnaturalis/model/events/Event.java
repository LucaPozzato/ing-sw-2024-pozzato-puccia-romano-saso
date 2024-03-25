package it.polimi.ingsw.codexnaturalis.model.events;

public abstract class Event {

    private int id;
    private long timeStamp;

    public int getEvent() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}