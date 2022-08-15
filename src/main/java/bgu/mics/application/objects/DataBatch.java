package bgu.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {
    private int start_index;
    private Data data;
    private boolean processed = false;
    private boolean trained = false;

    //Constructor
    public DataBatch(Data data, int start_index) {
        this.data = data;
        this.start_index = start_index;
    }


    public Data getData() {

        return data;
    }

    public void process() {
        if (!processed) {
            processed = true;
        } else {
            System.out.println("already processed!");
        }
    }

    public void train() {
        if (!trained) {
            trained = true;
        } else {
            System.out.println("already trained!");
        }

    }

    public boolean isProcessed() {
        return processed;
    }

    public boolean isTrained() {
        return trained;
    }
}
