package com.duong.chatapplication.mapper;

import com.duong.chatapplication.common.ConversationType;
import com.duong.chatapplication.dto.response.CreateConversationResponse;
import com.duong.chatapplication.dto.response.ParticipantResponse;
import com.duong.chatapplication.entity.Conversation;

public final class ConversationMapper {
    private ConversationMapper() {}

    public static CreateConversationResponse toConversationResponse(String creatorId, Conversation conversation) {
        ConversationType conversationType = conversation.getConversationType();

        CreateConversationResponse response = CreateConversationResponse.builder()
                .id(conversation.getId())
                .conversationType(conversationType)
                .participantInfo(conversation.getParticipants().stream()
                        .map(participant -> ParticipantResponse.builder()
                                .userId(participant.getUser().getId())
                                .username(participant.getUser().getUsername())
                                .build())
                        .toList())
                .createdAt(conversation.getCreatedAt())
                .build();

        if(conversationType == ConversationType.PRIVATE){
            // Với PRIVATE: tên là username của người còn lại (không phải creator)
            conversation.getParticipants().stream()
                    .filter(participant -> !participant.getUser().getId().equals(creatorId))
                    .findFirst()
                    .ifPresent(participantInfo -> response.setName(participantInfo.getUser().getUsername()));
        }
        else{
            // Với GROUP: dùng tên nhóm và avatar từ conversation
            response.setName(conversation.getName());
            response.setConversationAvatar(conversation.getConversationAvatar());
        }

        return response;
    }
}
