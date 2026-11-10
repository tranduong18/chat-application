package com.duong.chatapplication.mapper;

import com.duong.chatapplication.common.ConversationType;
import com.duong.chatapplication.dto.response.ConversationDetailResponse;
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

        // Resolve tên conversation
        String name = resolveConversationName(creatorId, conversation);
        response.setName(name);

        // Chỉ set avatar cho GROUP conversation
        if(conversationType == ConversationType.GROUP){
            response.setConversationAvatar(conversation.getConversationAvatar());
        }

        return response;
    }

    public static ConversationDetailResponse toConversationDetailResponse(String creatorId, Conversation conversation) {
        ConversationType conversationType = conversation.getConversationType();

        ConversationDetailResponse response = ConversationDetailResponse.builder()
                .id(conversation.getId())
                .conversationType(conversationType)
                .participantInfo(conversation.getParticipants().stream()
                        .map(participant -> ParticipantResponse.builder()
                                .userId(participant.getUser().getId())
                                .username(participant.getUser().getUsername())
                                .build())
                        .toList())
                .lastMessageId(conversation.getLastMessageId())
                .lastMessageContent(conversation.getLastMessageContent())
                .lastMessageTime(conversation.getLastMessageTime())
                .createdAt(conversation.getCreatedAt())
                .build();

        // Resolve tên conversation
        String name = resolveConversationName(creatorId, conversation);
        response.setName(name);

        // Chỉ set avatar cho GROUP conversation
        if(conversationType == ConversationType.GROUP){
            response.setConversationAvatar(conversation.getConversationAvatar());
        }

        return response;
    }

    // Helper method để resolve tên conversation
    // PRIVATE: Tên của người còn lại (không phải creatorId)
    // GROUP: Tên nhóm
    private static String resolveConversationName(String creatorId, Conversation conversation){
        if(conversation.getConversationType() == ConversationType.PRIVATE){
            return conversation.getParticipants().stream()
                    .filter(p -> !p.getUser().getId().equals(creatorId)) // Lọc người còn lại
                    .findFirst()
                    .map(p -> p.getUser().getUsername())// Lấy username
                    .orElse(null);
        }

        return conversation.getName(); // Trả về tên nhóm
    }
}
