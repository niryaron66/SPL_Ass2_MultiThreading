package bgu.mics.application.messages;

import bgu.mics.Broadcast;
import bgu.mics.application.objects.Model;

import java.util.Vector;

public class PublishConferenceBroadcast implements Broadcast {

    private static int priority = 2;
    Vector<Model> models;
    public PublishConferenceBroadcast(Vector<Model> models){
        this.models=models;

    }
    public Vector<Model> getModels() {
        return models;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
