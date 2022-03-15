package com.mau.model;

import java.util.HashMap;
import java.util.Map;

public enum EntityRepository {
    INSTANCE;

    Map<Integer, RandomEntity> records;

    EntityRepository(){
        records = new HashMap<Integer, RandomEntity>();
    }

    public Map<Integer, RandomEntity> getRecords() {
        return records;
    }

    public void setRecords(Map<Integer, RandomEntity> records) {
        this.records = records;
    }
}
