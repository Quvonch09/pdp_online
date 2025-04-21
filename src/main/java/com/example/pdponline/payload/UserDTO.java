package com.example.pdponline.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    private String imgUrl;

    private String role;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Long deviceId;
}
