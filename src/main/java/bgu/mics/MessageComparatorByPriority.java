package bgu.mics;

import java.util.Comparator;

public class MessageComparatorByPriority implements Comparator<Message> {

    @Override
    public int compare(Message a, Message b) {
        return Integer.compare(a.getPriority(), b.getPriority());

    }
}
