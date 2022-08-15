package bgu.mics.application.objects;

import bgu.mics.application.services.TimeService;

import java.util.Iterator;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {
    private Vector<GPU> gpus;
    private Vector<CPU> CPUs;
    private ConcurrentHashMap<DataBatch, GPU> dataBatchToGpu;
    private ConcurrentHashMap<CPU, BlockingQueue<DataBatch>> unProcessedQueues;

    ///////////////////////////Statitics////////////////////////////////

    private Vector<String> trainedModels;
    private int totalDataBatchProcessedCpu;
    private int cpuTimeUnitUsed;
    private int gpuTimeUnitUsed;
    private static Cluster cluster = null;

    //Constructor
    public Cluster() {
        gpus = new Vector<>();
        CPUs = new Vector<>();
        dataBatchToGpu = new ConcurrentHashMap<>();
        unProcessedQueues = new ConcurrentHashMap<>();
        trainedModels = new Vector<>();
        cpuTimeUnitUsed=0;
        gpuTimeUnitUsed=0;
        totalDataBatchProcessedCpu = 0;

    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Cluster getInstance() {
        if (cluster == null) {
            cluster = new Cluster();
        }
        return cluster;
    }
    //////////////// Getters ////////////////

    public BlockingQueue<DataBatch> getUnProcessedQueue(CPU cpu) {
      //  System.out.println("left in" + cpu.getName() + " queue " + unProcessedQueues.get(cpu).size());
        return unProcessedQueues.get(cpu);
    }

    public Vector<GPU> getGpus() {
        return gpus;
    }

    public int getTotalDataBatchProcessedCpu() {
        return totalDataBatchProcessedCpu;
    }

    public int getCpuTimeUnitUsed() {
        return cpuTimeUnitUsed;
    }

    public int getGpuTimeUnitUsed() {
        return gpuTimeUnitUsed;
    }

/////////////////////////////////////

    ///////////////////// Statistics //////////////////////////////
    public void updateTotalDataBatchProccessedbyCpu() {
        this.totalDataBatchProcessedCpu += 1;
    }

    public void addToTrainedModels(String name) {
        trainedModels.addElement(name);
    }


    public void addCpuTimeUnitUsed()
    {
        this.cpuTimeUnitUsed ++;
    }
    public void addGpuTimeUnitUsed() {
        this.gpuTimeUnitUsed++;
    }
    public void addToTotalDataBatchProccesedCPu()
    {
        totalDataBatchProcessedCpu++;
    }

    ///////////////////////////////////////////////////////////

    /////////////////////// Send to CPU  unProcessed Databatch ///////////////////////

    public void addToUnprocessedMap(DataBatch dataBatch, GPU gpu) {
        dataBatchToGpu.put(dataBatch, gpu); // add to hashmap to know who to return.
        CPU tmpMin = minFutureTime();
        unProcessedQueues.get(tmpMin).add(dataBatch); // add to best time cpu the databatch
//        System.out.println("add unprocesses Databatch To " + tmpMin.getName() );
      //
        // notifyAll();
       // System.out.println("debug");
//		if(dataBatchToGpu.containsKey(gpu)) {
//			dataBatchToGpu.get(gpu).addElement
//		}
// 		GPUtoModels.put(gpu, )
    }
    ///////////////////////////////////////////////////////////

    /////////////////////// Send to GPU  processed Databatch ///////////////////////

    public  void sendToGPU(DataBatch dataBatch) {
        synchronized (dataBatchToGpu.get(dataBatch)){
            dataBatchToGpu.get(dataBatch).reciveProcessedDataBatch(dataBatch);
            //   System.out.println("sending " + dataBatch + " to "  +  dataBatchToGpu.get(dataBatch));
            dataBatchToGpu.remove(dataBatch);
        }
    }

    ///////////////////////////////////////////////////////////


    ////////////// finding the best CPU to send him the DataBatch ////////////////////
    public  CPU minFutureTime() {
        int sum;
        int min = Integer.MAX_VALUE;

        if (cluster.CPUs.isEmpty())
            return null;
        CPU minTimeWork = cluster.CPUs.firstElement();
        synchronized (unProcessedQueues) {
            for (CPU key : unProcessedQueues.keySet()) {
                Queue q = unProcessedQueues.get(key);
                int numberOfCores = key.getNumberOfCores();
                Iterator<DataBatch> itr = q.iterator();
                sum = 0;
                while (itr.hasNext()) {
                    DataBatch db = itr.next();
                    if (db.getData().getType() == Data.Type.Images)
                        sum += (32 / numberOfCores) * 4;
                    if (db.getData().getType() == Data.Type.Text)
                        sum += (32 / numberOfCores) * 2;
                    if (db.getData().getType() == Data.Type.Tabular)
                        sum += (32 / numberOfCores);
                }
                if (sum < min) {
                    min = sum;
                    minTimeWork = key;
                }
            }
        }
        return minTimeWork;
    }
    ///////////////////////////////////////////////////////////

    //// Adding functions, using while constructing the CPU & GPU ///////

    public void addToCPUs(CPU cpu) {
        CPUs.addElement(cpu); // add Cpu to vector CPUs
        BlockingQueue<DataBatch> q = new LinkedBlockingQueue<>(); //Initialize queue for each CPU
        unProcessedQueues.put(cpu, q);
    }

    public void addToGPUS(GPU gpu) {
        gpus.addElement(gpu);
    }
    ///////////////////////////////////////////////////////////
}

