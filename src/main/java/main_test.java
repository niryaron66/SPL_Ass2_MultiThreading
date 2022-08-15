import bgu.mics.*;
import bgu.mics.application.messages.TickBroadCast;
import bgu.mics.application.messages.TrainModelEvent;
import bgu.mics.application.objects.*;
import bgu.mics.application.services.CPUService;
import bgu.mics.application.services.GPUService;

import java.util.concurrent.*;

public class main_test {
    public static void main(String []args) throws InterruptedException {
        //******************************************************************
//         ExecutorService ex = Executors.newFixedThreadPool(4);
//         Callable<Integer>[] vec = new Callable[7];
//         Future<Integer>[] futures = new Future[7];
//         for (int i=1 ; i <= 6 ; i++) {
//         int finalI = i;
//         vec[i] = () ->{
//         System.out.println
//         ("this is job number "+ finalI +", "+Thread.currentThread().getName()+" runs me");
//         return finalI;
//         };
//         }
//         for (int i=1 ; i <= 6 ; i++) {
//         try{
//         futures[i]= (Future<Integer>) ex.submit(vec[i]);
//         }catch (Exception e){
//         System.out.println(e);
//         }
//         }
//         ex.shutdown();
       // *************************** Future test ***************************************
//
//        Future<Integer> future = new Future<>();
//        Thread t1 = new Thread(()->{
//            System.out.println(Thread.currentThread().getName()+" is going to sleep...");
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            future.resolve(1);
//        });
//        Thread t3 = new Thread(()-> {
//            System.out.println(Thread.currentThread().getName()+" is working...");
//            System.out.println("before get: "+future.isDone());
//            future.get(3, TimeUnit.SECONDS);
////                future.get();
//            System.out.println("after get: "+future.isDone());
//
//        });
//        t1.start();
//        t3.start();
//        //*************************** TrainModelEvent Test ***************************************
//        MessageBusImpl msgbus = MessageBusImpl.getInstance();
//
//        Student student = new Student("1", "bgu" , "PhD");
//        GPU gpu = new GPU("RTX3090");
//        GPUService gpuService = new GPUService("Gpu1" , gpu);
//        CPU cpu = new CPU(32);
//        CPUService cpuService = new CPUService("CPu1", cpu);
//        Data data = new Data("Images",20000);
//        Model model = new Model("name", data,student);
//        Event<Model> trainModelEvent = new TrainModelEvent(model,"maor");
//        Broadcast<TickBroadCast>
//        msgbus.sendEvent(trainModelEvent);


//
//         Thread t1 = new Thread(() ->{
//              gpuService.run();
//         });
//
//        Thread t2 = new Thread(() ->{
//            cpuService.run();
//        });
//        Thread t3 = new Thread(() ->{
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            msgbus.sendEvent(trainModelEvent);
//        });
//        Thread t4 = new Thread(() ->{
//            try {
//                msgbus.awaitMessage(gpuService);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        t1.start();
//        t2.start();
//        t3.start();



//******************************************************************
//        Callable<Integer> call = ()->
//                                    {
//                                        System.out.println(1);
//                                        return 1;};
//        Runnable run = ()->{};
//        Thread thread = new Thread((Runnable) call);
////        Future<Integer> future =
    }
}