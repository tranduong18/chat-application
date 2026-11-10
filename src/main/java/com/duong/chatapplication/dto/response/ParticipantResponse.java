package com.duong.chatapplication.dto.response;

import lombok.Builder;

@Builder
public record ParticipantResponse(
        String userId,
        String username
) {
}
