package com.example.pdponline.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResNotification {

    private String title;

    private String content;
}
