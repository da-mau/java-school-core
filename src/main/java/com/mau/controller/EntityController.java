package com.mau.controller;

import com.mau.context.annotation.*;
import com.mau.http.handler.RequestHandler;
import com.mau.model.RandomEntity;
import com.mau.service.EntityService;

import java.util.List;

@RestController("/entities")
public class EntityController {
    @Autowired
    EntityService entityService;

    @GET(consumes = "application/json", produces = "application/json")
    public List<RandomEntity> getAll(){
        List<RandomEntity> result = entityService.getAll();
        return result;
    }

    @GET(path = "/{id}",consumes = "application/json", produces = "application/json")
    public RandomEntity getById(Integer id){
        RandomEntity result = entityService.getEntity(id);
        return result;
    }

    @POST(consumes = "application/json")
    public boolean createEntity(RandomEntity entity){

        return entityService.createEntity(entity);
    }

    @PUT(path = "/{id}", consumes = "application/json")
    public void updateEntity(Integer id, RandomEntity entity){
        entityService.updateEntity(entity);
    }

    @DELETE(consumes = "application/json")
    public void deleteEntity(Integer id){
        entityService.deleteEntity(id);
    }
}
