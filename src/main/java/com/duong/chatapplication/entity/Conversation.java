package com.duong.chatapplication.entity;

import com.duong.chatapplication.common.ConversationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ConversationParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();

    private String lastMessageId;

    private String lastMessageContent;

    private LocalDateTime lastMessageTime;

    public void addParticipant(User user) {
        participants.add(ConversationParticipant.builder()
                .conversation(this)
                .user(user)
                .build());
    }
}
