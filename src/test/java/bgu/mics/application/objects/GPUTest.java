//package bgu.mics.application.objects;
//
//import bgu.mics.MessageBusImpl;
//import bgu.mics.application.services.CPUService;
//import bgu.mics.application.services.GPUService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static bgu.mics.application.objects.GPU.Type.RTX3090;
//import static org.junit.jupiter.api.Assertions.*;
//
//class GPUTest {
//    private MessageBusImpl msgbus;
//    private GPU gpu;
//    private CPU cpu;
//    private CPUService cpuService;
//    private GPUService gpuService;
//    private Cluster cluster;
//    private Model model;
//    private Data data;
//    private Student student;
//
//    @BeforeEach
//    void setUp() {
//        msgbus = MessageBusImpl.getInstance();
//        cluster = Cluster.getInstance();
//        data = new Data("Images", 20000);
//        student = new Student("Student1", "bgu", "PhD");
//        model = new Model("model1", data, student);
//        gpu = new GPU("RTX3090");
//        cpu = new CPU(16);
//        gpuService = new GPUService("gpuser", gpu);
//        msgbus.register(gpuService);
//
//    }
//
//    @AfterEach
//    void tearDown() {
//        msgbus.unregister(gpuService);
//        /**///
//    }
//    @Test
//    void divide() {
//        gpu.divide(data);
//        int x = gpu.getAllDataBatches().size();
//        assertEquals(x*1000,data.getSize());
//   }
//
//    @Test
//    void TestsendUnprocessedDataBatchToCluster() {
//        DataBatch dataBatch = new DataBatch(model.getData(),0);
//        gpu.sendUnprocessedDataBatchToCluster(dataBatch);
//        assertTrue(cluster.getUnProcessedQueue(cpu).contains(dataBatch));
//    }
//
//    @Test
//    void reciveProcessedDataBatch() {
//        DataBatch dataBatch = new DataBatch(model.getData(),0);
//        dataBatch.process();
//        cpu.sendToCluster(dataBatch);
//        gpu.reciveProcessedDataBatch(dataBatch);
//        assertTrue(gpu.getvRam().contains(dataBatch));
//    }
////
////    @Test
////   void trainDataBatchModel() {
////        for (int i = 0 ; i < gpu.getvRam().size() ; i++){
////            assertNull(gpu.getvRam().get(i));
////        }
////        DataBatch dataBatch = new DataBatch(model.getData(),0);
////        dataBatch.process();
////        cluster.addToProcessed(dataBatch);
////        gpu.reciveProcessedDataBatch(dataBatch);
////        gpu.trainDataBatchModel(dataBatch);
////        assertFalse(gpu.getvRam().contains(dataBatch));
////    }
////
////    @Test
////    void testTrainDataBatchModel() {
////        DataBatch dataBatch = new DataBatch(model.getData(),0);
////        assertFalse(dataBatch.isTrained());
////        dataBatch.train();
////        assertTrue();
////    }
//}