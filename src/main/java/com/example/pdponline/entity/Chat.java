package com.example.pdponline.entity;

import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat extends AbsEntity {

    @Column(nullable = false)
    private Long sender;

    private Long receiver;

    @Column(columnDefinition = "text")
    private String content;

    private byte isRead;

    private byte isEdited;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> attachmentIds;

    @ManyToOne
    private Chat replayChat;

    @OneToOne
    private Task task;
}
