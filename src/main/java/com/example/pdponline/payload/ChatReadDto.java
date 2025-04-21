package com.example.pdponline.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatReadDto {

    private Long chatId;

    private boolean read;

    private Long senderId;

    private Long receiverId;

}
