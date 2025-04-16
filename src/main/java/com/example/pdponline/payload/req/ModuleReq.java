package com.example.pdponline.payload.req;

public record ModuleReq(
    String name,
    Long courseId,
    double price
){
}
