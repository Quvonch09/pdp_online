package com.example.pdponline.payload;

import com.example.pdponline.entity.enums.Role;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record MentorDto (
        Long id,
        String firstname,
        String lastname,
        Long imgId,
        Role role
) implements Serializable {
}
