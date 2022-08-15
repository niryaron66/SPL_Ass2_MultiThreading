package bgu.mics.example;

import bgu.mics.MicroService;

public interface ServiceCreator {
    MicroService create(String name, String[] args);
}
