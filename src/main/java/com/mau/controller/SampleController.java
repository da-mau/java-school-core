package com.mau.controller;

import com.mau.context.annotation.GET;
import com.mau.context.annotation.RestController;

import java.util.List;

@RestController("/samples")
public class SampleController {
    @GET(consumes = "application/json", produces = "application/json")
    public List<Object> getAll(){

        return null;
    }
}
