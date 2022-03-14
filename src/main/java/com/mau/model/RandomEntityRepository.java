package com.mau.model;

import java.util.HashMap;
import java.util.Map;

public class RandomEntityRepository {
    private static  Map<Integer, RandomEntity> records;

    public static  Map<Integer, RandomEntity> getRandomEntityRepo(){
        if(records == null){
            records = new HashMap<>();
        }
        return records;
    }

}
