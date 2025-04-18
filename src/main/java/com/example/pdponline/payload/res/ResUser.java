package com.example.pdponline.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResUser {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String password;

    private String imgUrl;
}
