package bgu.mics.application.objects;

import bgu.mics.Broadcast;
import bgu.mics.MessageBusImpl;
import bgu.mics.application.messages.TickBroadCast;
import bgu.mics.application.services.CPUService;
import bgu.mics.application.services.GPUService;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

class CPUTest {
    private CPU cpu;
    private GPU gpu;
    private DataBatch db;
    private Queue<DataBatch> queue;
    private Cluster cluster;
    private MessageBusImpl msgbus;
    private CPUService cpuService;
    private GPUService gpuService;


    @BeforeEach
    public void setUp() {
        msgbus = MessageBusImpl.getInstance();
        Data data = new Data("Images", 10000);
        db = new DataBatch(data, 0);
        queue = new LinkedList<DataBatch>();
        queue.add(db);
        cluster = new Cluster();
        cpu = new CPU("cpu1", 16);
        cpuService = new CPUService("CPUSER", cpu);
        gpu = new GPU("RTX3090");
        gpuService = new GPUService("gpu", gpu);
        gpu.sendUnprocessedDataBatchToCluster(db); //actually sending to cpu queue
        msgbus.register(cpuService);
    }

    @After
    public void tearDown() {
        msgbus.unregister(cpuService);
    }

    @Test
    void testGetUnprocessed() {
        DataBatch tmp = cluster.getUnProcessedQueue(cpu).poll();
        assertEquals(tmp, db);
        //DataBatch tmp2 = cpu.getUnprocessed();
        //  assertNull(tmp2);
    }

    @Test
    void testprocess() {
        Thread t1 = new Thread(() -> {
            cpuService.run();
        });
        Thread t2 = new Thread(() -> {
            TickBroadCast tickBroadCast = new TickBroadCast();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            msgbus.sendBroadcast(tickBroadCast);
            //cpuService.updateTick(tickBroadCast);
        });
        t1.start();
        t2.start();
        // int x=db.getData().getProcessed();
        //  assertFalse(db.isProcessed());
        //  cpu.process(db);
        assertTrue(db.isProcessed());
        //   assertEquals(db.getData().getProcessed()-x,1000);
        //    assertThrows("Should throw exception (DataBatch is already Processed)",Exception.class, () -> cpu.process(db));
    }
}

 //   @Test
//    void testsendToCluster() {
//        cpu.process(db);
//        cpu.sendToCluster(db);
//     //   assertEquals(cluster.getProcessedDataBatch().peek(), db);
//
//
//    }

//    //Todo: check if we need this?
//    @Test
//    void testcheckIfBusy() {
//        assertFalse(cpu.checkIfBusy());
//
//    }
//}