package com.mau.repository;

import com.mau.context.annotation.Repository;
import com.mau.model.RandomEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class EntityRepository {
    public List<RandomEntity> getAll() {
        List<RandomEntity> result = new ArrayList<>();
        Map<Integer, RandomEntity> records = getRecords();
        result.addAll(records.values());
        return result;
    }

    public RandomEntity findById(int id) {
        RandomEntity result = null;
        Map<Integer, RandomEntity> records = getRecords();
        if (records.containsKey(id)) {
            result = records.get(id);
        }
        return result;
    }

    public boolean create(RandomEntity entity) {
        boolean result;
        Map<Integer, RandomEntity> records = getRecords();
        if (!records.containsKey(entity.getId())) {
            records.put(entity.getId(), entity);
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean update(RandomEntity entity) {
        boolean result;
        Map<Integer, RandomEntity> records = getRecords();
        if (records.containsKey(entity.getId())) {
            result = true;
        } else {
            result = false;
        }
        records.put(entity.getId(), entity);
        return result;
    }

    public boolean delete(int id) {
        boolean result;
        Map<Integer, RandomEntity> records = getRecords();
        if (records.containsKey(id)) {
            records.remove(id);
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private Map<Integer, RandomEntity> getRecords() {
        EntityStorage repo = EntityStorage.INSTANCE;
        Map<Integer, RandomEntity> records = repo.getRecords();
        return records;
    }
}
