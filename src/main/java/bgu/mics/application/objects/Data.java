package bgu.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {

    private Type type;
    private int processed;
    private int size;
    private int trained;

    public int getTrained() {
        return trained;
    }

    public void updateTrained() {
      //  System.out.println(this +  "      updated     " + trained);
        trained += 1;
    }

    // Enum representing the Data type.
    enum Type {
        Images, Text, Tabular
    }

    //Constructor
    public  Data(String sType, int size) {
        if (sType.equals("images") || sType.equals("Images"))
            this.type = Type.Images;
        if (sType.equals("text") || sType.equals("Text"))
            this.type = Type.Text;
        if (sType.equals("tabular") || sType.equals("Tabular"))
            this.type = Type.Tabular;
        this.size = size;
        this.processed = 0;
        this.trained = 0;

    }


    public void updateProcessed() {
        processed += 1;
    }

    //////////////// Getters //////////////
    public Type getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public int getProcessed() {
        return processed;
    }

    public String toString(){
        String str = "";

        str += "\t\t\t\t\t\t\"type\": \"" + type + "\",\n";
        str += "\t\t\t\t\t\t\"size\": " + Integer.toString(size) + "\n";

        return str;
    }
    ///////////////////////////////////////
}
