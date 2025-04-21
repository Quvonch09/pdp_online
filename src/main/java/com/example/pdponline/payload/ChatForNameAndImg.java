package com.example.pdponline.payload;

public record ChatForNameAndImg(
        String senderName,
        String receiverName,
        String senderImg,
        String receiverImg
){}
