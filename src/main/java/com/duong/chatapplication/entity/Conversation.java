package com.duong.chatapplication.entity;

import com.duong.chatapplication.common.ConversationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Conversation extends BaseEntity{
    private String name;

    private String conversationAvatar;

    private ConversationType conversationType;

    @Column(name = "participant_hash", unique = true)
    private String participantHash; // 1_2 1_2
}
