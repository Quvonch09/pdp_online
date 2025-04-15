package com.example.pdponline.payload.req;

public record SectionReq (
        String title,
        Long moduleId,
        boolean alreadyOpen
){
}
