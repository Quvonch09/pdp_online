package com.example.pdponline.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Long imgId;

    private String role;
}
