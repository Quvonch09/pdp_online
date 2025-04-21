package com.example.pdponline.payload;

public record ChatMessageEditOrReplay(
        Long messageId,
        ChatDto chatDto
) {
}
