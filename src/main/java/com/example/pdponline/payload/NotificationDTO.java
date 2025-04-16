package com.example.pdponline.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {

    private String title;

    private String content;

    private Long userId;


}
