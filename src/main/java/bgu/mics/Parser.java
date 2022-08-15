package bgu.mics;

import bgu.mics.application.objects.*;
import bgu.mics.application.services.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class Parser {
    //TODO - read file and clean main
    // end of main is run for the student

    //fields
    private Student[] students;
    private List<Model[]> ListOfArraysOfModels;
    private GPU[] GPUArray;
    private CPU[] CPUArray;
    private ConfrenceInformation[] confrenceInformations;
    private int TickTime;
    private int Duration;

    public Parser(){}

    public void readInputFile(String pathName){

        File input = new File(pathName);
        try {
            JsonElement element = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = element.getAsJsonObject();

            JsonArray jsonArrayOfStudents = fileObject.get("Students").getAsJsonArray();

            students = new Student[jsonArrayOfStudents.size()];
            ListOfArraysOfModels = new ArrayList<Model[]>();


            int counterOfStudents = 0;
            int counterOfModel = 0;
            int counterOfGPU = 0;
            int counterOfCPU = 0;
            int counterOfConfernces = 0;
            for(JsonElement studentElement : jsonArrayOfStudents){

                JsonObject studentJsonObject = studentElement.getAsJsonObject();
                String name = studentJsonObject.get("name").getAsString();
                String department = studentJsonObject.get("department").getAsString();
                String status = studentJsonObject.get("status").getAsString();

                Student student= new Student(name,department, status);
                JsonArray jsonArrayOfModels = studentJsonObject.get("models").getAsJsonArray();
                Model[] models = new Model[jsonArrayOfModels.size()];
                for(JsonElement modelElement : jsonArrayOfModels){
                    JsonObject modelJsonObject = modelElement.getAsJsonObject();
                    String modelName = modelJsonObject.get("name").getAsString();

                    String data_type = modelJsonObject.get("type").getAsString();
                    String data_size = modelJsonObject.get("size").getAsString();
                    int data_size_int = Integer.parseInt(data_size);
                    Data data = new Data(data_type, data_size_int);

                    models[counterOfModel] = new Model(modelName,data,student);
                    counterOfModel++;
                }
                counterOfModel = 0;

                String sSname=name+" SERVICE";
                StudentService studentService= new StudentService(sSname,student);
                students[counterOfStudents]=student;
                ListOfArraysOfModels.add(models);
                counterOfStudents++;

            }
            //*********************** getting GPUs ***********************
            JsonArray JsonArrayOfGpu = fileObject.get("GPUS").getAsJsonArray();
             GPUArray = new GPU[JsonArrayOfGpu.size()];

            for(JsonElement GPUElement : JsonArrayOfGpu){
                String type = GPUElement.getAsString();
                GPU gpu=new GPU(type);
                String gName=type + " SERVICE";
                GPUService gps=new GPUService(gName,gpu);
                GPUArray[counterOfGPU] = gpu;
                counterOfGPU++;
            }
            //****************** getting CPUs ***********************
            JsonArray JsonArrayOfCpu = fileObject.get("CPUS").getAsJsonArray();

            CPUArray = new CPU[JsonArrayOfCpu.size()];

            for(JsonElement CPUElement : JsonArrayOfCpu){
                int cores = CPUElement.getAsInt();
                String cName= "CPU" +String.valueOf(counterOfCPU);
                CPU cpu=new CPU(cName,cores);
                String cServiceName="CPUSERVICE" + String.valueOf(counterOfCPU);
                CPUService cps=new CPUService(cServiceName,cpu);
                CPUArray[counterOfCPU] = cpu;
                counterOfCPU++;
            }
            //*********************** getting Conferences***********************
            JsonArray JsonArrayOfConferences = fileObject.get("Conferences").getAsJsonArray();
            confrenceInformations = new ConfrenceInformation[JsonArrayOfConferences.size()];
            for(JsonElement ConferenceElement : JsonArrayOfConferences){
                JsonObject ConferenceJsonObject = ConferenceElement.getAsJsonObject();
                String name = ConferenceJsonObject.get("name").getAsString();
                int date = Integer.parseInt(ConferenceJsonObject.get("date").getAsString());
                ConfrenceInformation conf=new ConfrenceInformation(name,date);
                confrenceInformations[counterOfConfernces] = conf;
                ConferenceService confService=new ConferenceService(name +"Service",conf);
                counterOfConfernces++;
            }
            //*********************** getting TickTime&Duration ***********************
            TickTime = Integer.parseInt(fileObject.get("TickTime").getAsString());
            Duration = Integer.parseInt(fileObject.get("Duration").getAsString());
            TimeService timeService=new TimeService(50,5500);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Student[] getStudents() {
        return students;
    }

    public List<Model[]> getListOfArraysOfModels() {
        return ListOfArraysOfModels;
    }

    public GPU[] getGPUArray() {
        return GPUArray;
    }

    public CPU[] getCPUArray() {
        return CPUArray;
    }

    public ConfrenceInformation[] getConfrenceInformations() {
        return confrenceInformations;
    }

    public int getTickTime() {
        return TickTime;
    }

    public int getDuration() {
        return Duration;
    }


}
