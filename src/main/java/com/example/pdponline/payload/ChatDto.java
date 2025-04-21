package com.example.pdponline.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class ChatDto {
    private Long id;
    private Long sender;
    private Long receiver;
    private Long taskId;
    private String content;
    private boolean isRead;
    private String senderName;
    private String receiverName;
    private String senderImg;
    private String receiverImg;
    private String createdAt;
    private Set<Long> attachmentIds;
    private boolean isEdited;
    private ChatDto replayDto;


    @JsonCreator
    public ChatDto(@JsonProperty("id") Long id,
                   @JsonProperty("sender") Long sender,
                   @JsonProperty("receiver") Long receiver,
                   @JsonProperty("taskId") Long taskId,
                   @JsonProperty("content") String content,
                   @JsonProperty("isRead") boolean isRead,
                   @JsonProperty("senderName") String senderName,
                   @JsonProperty("receiverName") String receiverName,
                   @JsonProperty("senderImg") String senderImg,
                   @JsonProperty("receiverImg") String receiverImg,
                   @JsonProperty("createdAt") String createdAt,
                   @JsonProperty("attachmentIds") Set<Long> attachmentIds,
                   @JsonProperty("isEdited") boolean isEdited,
                   @JsonProperty("replayDto") ChatDto replayDto)
    {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.taskId = taskId;
        this.content = content;
        this.isRead = isRead;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
        this.createdAt = createdAt;
        this.attachmentIds = attachmentIds;
        this.isEdited = isEdited;
        this.replayDto = replayDto;

    }
}
