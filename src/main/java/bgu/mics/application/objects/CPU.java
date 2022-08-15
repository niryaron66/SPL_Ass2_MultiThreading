package bgu.mics.application.objects;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    private String name;
    private int numberOfCores;
    private Cluster cluster;
    private DataBatch db = null;
    private int tickForAction;
    private int processingTick;

    //Constructor
    public CPU(String name, int numberOfCores /**, Cluster cluster */) {
        this.name = name;
        this.numberOfCores = numberOfCores;
        this.cluster = Cluster.getInstance();
        this.processingTick = Integer.MAX_VALUE;
        this.tickForAction = 0;
        cluster.addToCPUs(this);
    }

    /**
     * @return
     * @pre: dataBatch.isProcessed == false
     * @post: dataBatch.isProcessed == true AND  data.proccesed=@pre data.processed+1000
     */
    //////// MAIN FUNCTION: process the databatch with ticks ////////
    public  DataBatch processDatabatch() {
     //   System.out.println(this.getName()+ " trying to Process");
        if(db!=null) {
        //    System.out.println(this.getName() + " is processing");
            processFunction();
        }
        else {
            try {
                db = cluster.getUnProcessedQueue(this).take();
//                processFunction();
            } catch (InterruptedException e) {
          //      System.out.println("didnt take from queue");
            }
            if(db!=null)
                processFunction();
        }
        return db;
    }

    public  DataBatch processFunction (){
        cluster.addCpuTimeUnitUsed();
    //System.out.println(this.getName()+ " im busy with: " + db.toString());
            switch (db.getData().getType()) { // why noy WHILE AND WAIT ?
                case Images:
                    //System.out.println(this.getName() +" Images" );
                    processingTick = 32 / numberOfCores * 4 - tickForAction;
                   // System.out.println("processingTick: " + processingTick);
                   // System.out.println("tickForAction: " + tickForAction);
                    if (processingTick == 0) {
                        tickForAction = 0;
                        db.process();
                        doneProcessThisBatch(db);
                    }
                    break;
                case Text:

                    processingTick = 32 / numberOfCores * 2 - tickForAction;
                    if (processingTick == 0) {
                        tickForAction = 0;
                        db.process();
                        doneProcessThisBatch(db);


                    }
                    break;
                case Tabular:

                    processingTick = 32 / numberOfCores - tickForAction;
                    if (processingTick == 0) {
                        tickForAction = 0;
                        db.process();
                        doneProcessThisBatch(db);

                    }
                    break;
            }
        return db;
    }


    public  void doneProcessThisBatch(DataBatch dataBatch) {
        cluster.addToTotalDataBatchProccesedCPu();
//        System.out.println(getName() + " is done processing dataBatch " + db + " or " + dataBatch);
      //  System.out.println(this.getName() + " " + db.getData().getProcessed());
        db.getData().updateProcessed();
        sendToCluster(db);
        db = null;
    }
    /////////////////////////////////////////////////////////////////////




    // send to cluster processed databatch and then send to relevant GPU //

    /**
     * @param processedDataBatch
     * @pre: dataBatch.isProcessed == true
     * @post: cluster.processedBatch != null
     */
    public  void sendToCluster(DataBatch processedDataBatch) {
       // System.out.println("done process");
        this.cluster.sendToGPU(processedDataBatch);
    }
    ///////////////////////////////////////////////////////////


    public void updateTick(int tick) {
            tickForAction++;
        processDatabatch();
    }



    //////////// Getters ///////////

    public Cluster getCluster() {
        return cluster;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public String getName() {
       // System.out.println("my name is: " +name);
        return name;
    }

    public DataBatch getProcessingDataBatch() {
        return db;
    }

    ////////////////////////////////
}


