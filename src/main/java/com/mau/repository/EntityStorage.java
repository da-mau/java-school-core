package com.mau.repository;

import com.mau.model.RandomEntity;

import java.util.HashMap;
import java.util.Map;

public enum EntityStorage {
    INSTANCE;

    Map<Integer, RandomEntity> records;

    EntityStorage(){
        records = new HashMap<Integer, RandomEntity>();
    }

    public Map<Integer, RandomEntity> getRecords() {
        return records;
    }

}
