package bgu.mics.example.messages;

import bgu.mics.Broadcast;

public class ExampleBroadcast implements Broadcast {

    private String senderId;

    public ExampleBroadcast(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

    public int getPriority() {
        return 0;
    }
}
