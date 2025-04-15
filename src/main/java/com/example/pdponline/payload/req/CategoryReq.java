package com.example.pdponline.payload.req;

import java.util.List;

public record CategoryReq (
    String name,
    String description,
    int duration,
    List<Long> mentorIds
){
}
