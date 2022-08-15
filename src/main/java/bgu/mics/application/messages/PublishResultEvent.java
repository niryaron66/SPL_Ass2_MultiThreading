package bgu.mics.application.messages;

import bgu.mics.Event;
import bgu.mics.application.objects.Model;

public class PublishResultEvent implements Event<Model> {
    private static int priority = 2;
    private Model m;
    public PublishResultEvent(Model m) {
        this.m=m;
    }
    public Model getModel()
    {
        return this.m;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
