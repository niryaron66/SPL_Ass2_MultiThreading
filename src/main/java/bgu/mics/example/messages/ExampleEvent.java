package bgu.mics.example.messages;

import bgu.mics.Event;

public class ExampleEvent implements Event<String> {

    private String senderName;

    public ExampleEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public int getPriority() {
        return 0;
    }
}