package bgu.mics.application.services;

import bgu.mics.MessageBus;
import bgu.mics.MessageBusImpl;
import bgu.mics.MicroService;
import bgu.mics.application.messages.PublishConferenceBroadcast;
import bgu.mics.application.messages.PublishResultEvent;
import bgu.mics.application.messages.TerminateBroadcast;
import bgu.mics.application.messages.TickBroadCast;
import bgu.mics.application.objects.ConfrenceInformation;
import bgu.mics.application.objects.Model;

import java.util.Iterator;
import java.util.Vector;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConfrenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    private ConfrenceInformation conf;
    private int ticks;
    private Vector<Model> models;
    private int date;

    public ConferenceService(String name,ConfrenceInformation conf) {
        super(name);
        this.conf=conf;
        ticks=0;
        this.date = conf.getSetDate();
        models =new Vector<>();
        // TODO Implement this
    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);
        subscribeBroadcast(TickBroadCast.class, (TickBroadCast tickBroadCast) -> {
            ticks ++;
            if (ticks == date) {
                sendBroadcast(new PublishConferenceBroadcast(models));
                terminate();
            }

        });

        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast terminateBroadcast) -> {
            this.terminate();
        });

        subscribeEvent(PublishResultEvent.class, (PublishResultEvent publishResultEvent)-> {
          //  System.out.println(publishResultEvent.getModel());
            models.add(publishResultEvent.getModel());
            conf.addPublication(publishResultEvent.getModel());
        });

        if(ticks==conf.getSetDate())
            MessageBusImpl.getInstance().sendBroadcast(new PublishConferenceBroadcast(models));

        // TODO Implement this

    }
}
