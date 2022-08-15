package bgu.mics.application;

import bgu.mics.Callback;
import bgu.mics.MessageBusImpl;
import bgu.mics.MicroService;
import bgu.mics.Parser;
import bgu.mics.application.messages.TrainModelEvent;
import bgu.mics.application.objects.*;
import bgu.mics.application.services.*;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;


/**
 * This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {

    public static CountDownLatch threadInitCounter;
    public static void main(String[] args) {
        Parser reader = new Parser();
        reader.readInputFile("example_input4.json");  //the input path is starting from the folder of the project!


//         Reading input file

        Student[] studentArray = reader.getStudents();
        GPU[] gpuArray = reader.getGPUArray();
        CPU[] cpuArray = reader.getCPUArray();
        ConfrenceInformation[] conferenceArray = reader.getConfrenceInformations();
        int TickTime = reader.getTickTime();
        int Duration = reader.getDuration();


//


        Thread[] studentServices = new Thread[studentArray.length];
        Thread[] CPUServices = new Thread[cpuArray.length];
        Thread[] GPUServices = new Thread[gpuArray.length];
        Thread[] confrencesServices = new Thread[conferenceArray.length];


//      register micro services

        int allTread = GPUServices.length + confrencesServices.length + CPUServices.length;
        threadInitCounter = new CountDownLatch(allTread);
        MicroService timer = new TimeService(TickTime , Duration);
        for (int i = 0; i < studentServices.length; i++) {
            MicroService tmpservice = new StudentService(studentArray[i].getName(), studentArray[i]);
            studentServices[i] = new Thread(tmpservice);

        }
        for (int i = 0; i < CPUServices.length; i++) {
            MicroService tmpservice = new CPUService("CPU" + i, cpuArray[i]);
            CPUServices[i] = new Thread(tmpservice);
        }
        for (int i = 0; i < GPUServices.length; i++) {
            MicroService tmpservice = new GPUService("GPU" + i, gpuArray[i]);
            GPUServices[i] = new Thread(tmpservice);
        }
        for (int i = 0; i < confrencesServices.length; i++) {
            MicroService tmpservice = new ConferenceService(conferenceArray[i].getName(), conferenceArray[i]);
            confrencesServices[i] = new Thread(tmpservice);
        }


//          Running microSerivce

        Thread clock = new Thread(timer);
        Cluster cluster = Cluster.getInstance();
        for (Thread cpuService : CPUServices) {
            cpuService.start();
        }
        for (Thread gpuService : GPUServices) {
            gpuService.start();
        }
        for (Thread confrencesService : confrencesServices) {
            confrencesService.start();
        }
        try{
            Thread.currentThread().sleep(300);}
        catch (Exception ex){}

        for (int i=0 ;i< studentServices.length ; i++){
            studentServices[i].setName(studentArray[i].getName());
            studentServices[i].start();
        }

        try{
            Thread.currentThread().sleep(300);}
        catch(Exception ex){}

        clock.start();

        try {
            clock.join();
        }catch (Exception exz){}

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for(Thread th : threadSet){
//            if(th.isAlive())
            try {
                th.interrupt();
            }catch(Exception ex){}
        }




//        boolean b = true;
//        while (b) {
//            if (!clock.isAlive())
//                b = false;
//        }

        File output = new File("C:\\Users\\maorb\\OneDrive\\Ben-Gurion\\SPL\\HW2\\files\\output.txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter(output);

            //writing the students into the output file
            writer.write("{\n\t\"students\": [");
            for (int i = 0; i < studentArray.length; i++) {
                writer.write("\n\t\t{\n\t\t");
                writer.write(studentArray[i].toString());
                writer.write("\n\t\t}");
                if (i < studentArray.length - 1)
                    writer.write(",");
            }
            writer.write("\n\t],\n");

            //writing the conferences into the output file
            writer.write("\t\"conferences\": [\n");
            for (int i = 0; i < conferenceArray.length; i++) {
                writer.write("\t\t{\n\t\t");
                writer.write(conferenceArray[i].toString());
                writer.write("\n\t\t}");
                if (i < conferenceArray.length - 1)
                    writer.write(",");
                writer.write("\n");
            }
            writer.write("\t],\n");

//            writing the entire data
            writer.write("\t\"cpuTimeUnitUsed\": ");
            writer.write(Integer.toString(cluster.getCpuTimeUnitUsed()));
            writer.write(",\n");

            writer.write("\t\"gpuTimeUnitUsed\": ");
            writer.write(Integer.toString(cluster.getGpuTimeUnitUsed()));
            writer.write(",\n");

            writer.write("\t\"totalDataBatchProcessedCpu\": ");
            writer.write(Integer.toString(cluster.getTotalDataBatchProcessedCpu()));
            writer.write(",\n");

            writer.write("}");

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
//
    }


}
