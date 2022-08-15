package bgu.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {
    private String name;
    private Student student;
    private Data data;
    private Result res;
    private Status status;

    public String getRes() {
        if(res==Result.Good)
            return "Good";
      else
          return "Bad";
    }

    public void setRes(String res) {
        if(res=="Good")
            this.res = Result.Good;
        else if (res=="Bad")
            this.res=Result.Bad;
    }

    public Student getStudent() {
        return student;
    }

    enum Status {
        PreTrained, Training, Trained, Tested
    }

    public String getStatus() {
        if (status == Status.PreTrained)
            return "PreTrained";

        if (status == Status.Training)
            return "Training";

        if (status == Status.Trained)
            return "Trained";

        else {
            return "Tested";
        }
    }

    public void setStatus(String sStatus) {

        switch (sStatus) {
            case "PreTrained":
                status = status.PreTrained;
                break;
            case "Training":
                status = status.Training;
                break;
            case "Trained":
                status = status.Trained;
                break;
            case "Tested":
                status = status.Tested;
                break;
        }

    }

    enum Result {
        None, Good, Bad
    }


    //Constructor
    public Model(String name, Data data, Student student) {
        this.name = name;
        this.data = data;
        this.student = student;
        this.res = Result.None;
        this.status = Status.PreTrained;
        student.addModel(this);

    }
    public boolean isGood()
    {
        return this.res==Result.Good;
    }


    public String getName() {
        return name;
    }

    public Data getData() {
        return data;
    }
    public String toString(){
        String str = "";

        str += "\"name\": \"" + name + "\",\n";
        str += "\t\t\t\t\t\"data\": {\n" + data.toString() + "\t\t\t\t\t},\n";
        str += "\t\t\t\t\t\"status\": \"" + status + "\",\n";
        str += "\t\t\t\t\t\"results\": \"" + res + "\"";

        return str;
    }
}
