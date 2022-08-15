package bgu.mics.application.objects;

import java.util.Arrays;
import java.util.Vector;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {


    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}


    public Type getType() {
        return type;
    }

    private Type type;
    private Model model;
    private Cluster cluster;
    private int trainingTime = Integer.MAX_VALUE;
    private Vector<DataBatch> allDataBatches;
    private DataBatch trainingDatabatch;
    private int totalCurrentModelTrained;
    private Vector<DataBatch> vRam;
    //private int currentProcessInVram;
    private int wannabeInVram;
    private int tickForAction;

    private int size;

    public GPU(String sType) {
        super();
        vRam = new Vector<>();
        if (sType.equals("RTX3090")) {
            this.type = Type.RTX3090;
            //vRam.setSize(32);
            size=32;
        }
        if (sType.equals("RTX2080")) {
            this.type = Type.RTX2080;
            //vRam.setSize(16);
            size=16;
        }
        if (sType.equals("GTX1080")) {
            this.type = Type.GTX1080;
           // vRam.setSize(8);
            size=8;
        }
        this.cluster = Cluster.getInstance();
        cluster.addToGPUS(this);
        wannabeInVram = 0;
        totalCurrentModelTrained = 0;
    }

    /////////////////Getters///////////////////

    public Vector<DataBatch> getAllDataBatches() {
        return allDataBatches;
    }

    public Vector<DataBatch> getvRam() {
        return vRam;
    }

    public int getWannabeInVram() {
        return wannabeInVram;
    }

    ////////////////////////////////////////////

    /**
     * @return
     * @pre:none
     * @post: size.Vector<DataBatch>*1000  = Data.size
     */
    public void divide(Data data) {
        allDataBatches = new Vector<>();

        for (int i = 0; i < data.getSize(); i += 1000) {
            DataBatch db = new DataBatch(data, i);
            allDataBatches.addElement(db);
        }
//        int size = data.getSize() / 1000; //Todo: check if we can to assume its only divine in 1000
    }


    //////////////// Send to Cluster ///////////

    /**
     * @return
     * @pre: none
     * @post: cluster.unProcessedData.contain()
     */
    public void sendUnprocessedDataBatchToCluster(DataBatch db) {
//      Decide how to send - ALREADY DONE IN GPU SERVICE!
        cluster.addToUnprocessedMap(db, this);
        wannabeInVram++;

    }

    ////////////////////////////////////////////

    /////////////// Get Processed DataBatch to VRAM ////////////////////

    /**
     * @return
     * @pre: cluster.getProcessDataBatch != null
     * @post: vram contains @pre head of queue.
     */
    public  void reciveProcessedDataBatch(DataBatch proDB) {
        synchronized (vRam) {
          //  System.out.println("reciving to Vram " + proDB);
            if(vRam.size()<size)
          //  for (int i = 0; i < size; i++) {
          //      if (vRam.get(i) == null) {
                    vRam.addElement(proDB);
                    //System.out.println("recived " + proDB);
                 //   break;
        //        }
            }
        }



    /////////////////////////////////////////////////////////////////


    ////////////////////////Train the processed Databatch ////////////

    /**
     * @return
     * @pre:the databatch is untrained and processed
     * @post: databatch is trained .
     */
    public  void trainDataBatch() {

        if (trainingDatabatch != null) {

                  //    System.out.println(this + " is Training" + "the " + trainingDatabatch);
            trainFunction();
        } else {
            if(!vRam.isEmpty()){
                    trainingDatabatch = vRam.firstElement();
                    trainFunction();
                }



            }
        }

    public DataBatch trainFunction (){
        cluster.addGpuTimeUnitUsed();
        switch (this.getType()) {
            case RTX3090:
                trainingTime = 1 - tickForAction;
                if (trainingTime == 0) {
                    //System.out.println("3090 total trained "+totalCurrentModelTrained);
                    doneTrainThisBatch();
                }
            case RTX2080:

                trainingTime = 2 - tickForAction;
                if (trainingTime == 0) {
                  //  System.out.println("2080 total traind: " + totalCurrentModelTrained);
                    doneTrainThisBatch();
                }
            case GTX1080:
                trainingTime = 4 - tickForAction;
                if (trainingTime == 0) {
                 //   System.out.println("1080 total traind: " + totalCurrentModelTrained);
                  //  System.out.println(totalCurrentModelTrained);
                    doneTrainThisBatch();
                }
        }
        return trainingDatabatch;
    }
    public void doneTrainThisBatch() {
    //    System.out.println(this.getType() + " TRAINING BATCH OF : " + model.getName() + " ALREADY TRAINED DATA SIZE IS : " + trainingDatabatch.getData().getTrained());  ///////////////////////////
        if (model.getStatus().equals("PreTrained"))
            model.setStatus("Training");
        tickForAction = 0;
      //  System.out.println(this + " my Model " + trainingDatabatch.getData().getTrained());
        trainingDatabatch.getData().updateTrained();
//        System.out.println(this.getType() + " updateTrained: " + model.getName());
        totalCurrentModelTrained++;
        vRam.remove(trainingDatabatch);
        trainingDatabatch = null;
        wannabeInVram--;

            }


    public void updateTick(int tick) {
        if (!vRam.isEmpty())
              tickForAction++;
        if (model == null) {
            return;
        }
        //System.out.println(this.getType() + " trained: "+model.getName()+ " " +totalCurrentModelTrained);
        //System.out.println("total size of: "+model.getName()+ " " + model.getData().getSize()/1000);
        if(totalCurrentModelTrained==model.getData().getSize()/1000) {
            //System.out.println("trained");
            this.getModel().setStatus("Trained");
            totalCurrentModelTrained=0;
        }
        if (!model.getStatus().equals("Trained")) {

            if (!this.getAllDataBatches().isEmpty()) {
                int freeSpace = size - this.getWannabeInVram();
                for (int i = 0; i < freeSpace; i++) {
                    if (!this.getAllDataBatches().isEmpty()) {

                        this.sendUnprocessedDataBatchToCluster(this.getAllDataBatches().remove(0));// TODO CHECK IF REMOVE WORK?
                    }
                }
            }

        }
        trainDataBatch();

    }


    public void setModel(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }
    public boolean isBusy() {
        return trainingDatabatch!=null;
    }
///////////////////////////////////////////////////////////////////


}
