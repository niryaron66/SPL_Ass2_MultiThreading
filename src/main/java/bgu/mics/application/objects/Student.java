package bgu.mics.application.objects;

import bgu.mics.Future;
import bgu.mics.MicroService;
import bgu.mics.application.services.StudentService;

import java.util.Iterator;
import java.util.Vector;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private Vector<Model> models;
    private Vector<Model> trainedModels;
     private Future<Model> future;
    private int testedCounter;

    //Constructor
    public Student(String name, String department, String sStatus) {
        this.name=name;
        this.department = department;
        if (sStatus.equals("MSc"))
            this.status = Degree.MSc;
        if (sStatus.equals("PhD"))
            this.status = Degree.PhD;

        this.publications = 0;
        this.papersRead = 0;
        models=new Vector<>();
        trainedModels=new Vector<>();
    }

    public String getStatus() {
        if (status ==  Degree.MSc)
                return "MSc";
        else
            return "PhD";
    }


    public void incrementPublished() {
        publications++;
    }

    public void readPaper() {
        papersRead++;
    }

    public int getTestedCounter() {
        return testedCounter;
    }

    public void incrementTestedCounter () {
        this.testedCounter++;
    }

    // Enum representing the Degree the student is studying for.
    enum Degree {
        MSc, PhD
    }

    public void addToTrainedModel(Model model) {
       // System.out.println("FINISH "+ model);
        this.trainedModels.addElement(model);
    }




    public Vector<Model> getModels()
    {
        return models;
    }
    public String getName()
    {
        return name;
    }
    public void addModel(Model model)
    {
        models.addElement(model);
    }


    public Future<Model> getFuture() {
        return future;
    }

    public void setFuture(Future<Model> future) {
        this.future = future;
    }

    public String toString(){
        String output = "";

        output += "\t\"name\": \"" + name + "\",\n";
        output += "\t\t\t\"department\": \"" + department + "\",\n";
        output += "\t\t\t\"status\": \"" + status + "\",\n";
        output += "\t\t\t\"publications\": " + Integer.toString(publications) + ",\n";
        output += "\t\t\t\"papersRead\": " + Integer.toString(papersRead) + ",\n";
        output += "\t\t\t\"trainedModels\": [\n\t\t\t\t";
        if (models.size() > 0){
            Iterator<Model> itr = models.iterator();

            while (itr.hasNext()){
                Model m = itr.next();
                while ( m.getStatus() .equals("Trained")){
                    if ( itr.hasNext() )
                        m = itr.next();
                    else
                        break;
                }
                output += "{\n\t\t\t";
                output += "\t\t" + m.toString() + "\n\t\t\t\t}";
                output += ",\n\t\t\t\t";
            }
            output = output.substring(0, output.length() - 6);
            output += "\n";
        }
        output += "\t\t\t]";

        return output;
    }
}
