package bgu.mics.application.objects;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private int ticksFromService;
    private Vector<Model> publication;
    private String name;
    private int setDate;

    //TODO Need Events Or NOT ??????????
    public ConfrenceInformation(String name, int setDate) {
        this.name = name;
        this.setDate = setDate;
        ticksFromService=0;
        publication=new Vector<>();

    }

    public void addPublication(Model model) // TODO Check when to add model
    {
       publication.add(model);
    }

    public Vector<Model> getModels() {
        return publication;
    }

    public void updateTick(int tick) {
        this.ticksFromService = tick;
    }

    public boolean needToPublish()
    {
        return this.setDate==ticksFromService;
    }

    public int getSetDate() {
        return setDate;
    }
    public String getName()
    {
        return name;
    }
    public String toString() {
        String str = "";

        str += "\t\"name\": \"" + name + "\",\n";
        str += "\t\t\t\"date\": " + Integer.toString(setDate) + ",\n";
        str += "\t\t\t\"publications\": [";
        for (int i = 0; i < publication.size(); i++){
            str += "{\n\t\t\t" + publication.get(i).toString() + "\n}";
            if ( i < publication.size() - 1 )
                str += ",";
        }
        str += "]";
        return str;
    }
}
