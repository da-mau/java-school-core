package com.mau.service;

import com.mau.context.annotation.Autowired;
import com.mau.context.annotation.Service;
import com.mau.controller.EntityController;
import com.mau.model.RandomEntity;
import com.mau.repository.EntityRepository;

import java.util.List;

@Service
public class EntityService {
    @Autowired
    EntityRepository entityRepository;

    public List<RandomEntity> getAll(){
        return entityRepository.getAll();
    }

    public RandomEntity getEntity(int id) {
        return entityRepository.findById(id);
    }

    public boolean createEntity(RandomEntity entity) {
        return entityRepository.create(entity);
    }

    public boolean updateEntity(RandomEntity entity) {
        return entityRepository.update(entity);
    }

    public boolean deleteEntity(int id) {
        return entityRepository.delete(id);
    }

}
