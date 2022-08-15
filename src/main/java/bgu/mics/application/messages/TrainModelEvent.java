package bgu.mics.application.messages;

import bgu.mics.Event;
import bgu.mics.Future;
import bgu.mics.application.objects.Model;

public class TrainModelEvent implements Event<Model> {
    private static int priority = 2;
    private String senderName;
    private Model model;
    private Future<Model> future;

    public TrainModelEvent(Model model, String senderName) {
        this.senderName = senderName;
        this.model = model;
        this.future=new Future<>();
    }

    public Model getModel() {
        return model;
    }

    public String getSenderName() {
        return senderName;
    }

    public Future<Model> getFuture() { return future; }

    @Override
    public int getPriority() {
        return priority;
    }
}

