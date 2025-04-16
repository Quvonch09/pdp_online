package com.example.pdponline.payload;

import com.example.pdponline.entity.enums.Role;
import lombok.Builder;

@Builder
public record MentorDto (
        Long id,
        String firstname,
        String lastname,
        Long imgId,
        Role role
){
}
