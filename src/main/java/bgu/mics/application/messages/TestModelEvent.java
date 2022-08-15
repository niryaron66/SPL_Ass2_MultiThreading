package bgu.mics.application.messages;

import bgu.mics.Event;
import bgu.mics.Future;
import bgu.mics.application.objects.Model;

public class TestModelEvent implements Event<Model> {
    private static int priority = 1;
    private Model m;
    private String name;
    private Future<Model> f;

    public TestModelEvent(Model model,String name) {
        this.m=model;
        this.name=name;
        f=new Future<>();
    }

    public String getName() {
        return this.name;
    }

    public Model getModel() {
        return m;
    }

    public Future<Model> getFuture() {
        return this.f;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
