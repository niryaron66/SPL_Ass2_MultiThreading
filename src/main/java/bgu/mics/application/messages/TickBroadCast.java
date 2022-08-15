package bgu.mics.application.messages;

import bgu.mics.Broadcast;

public class TickBroadCast implements Broadcast {
    private final static int priority = 2; //big letters
    private int tick;

    public TickBroadCast() {
    }

    public int getTick() {
        return tick;
    }
    @Override
    public int getPriority() {
        return priority;
    }

}
