package com.duong.chatapplication.controller;

import com.duong.chatapplication.dto.request.CreateConversationRequest;
import com.duong.chatapplication.dto.response.ApiResponse;
import com.duong.chatapplication.dto.response.CreateConversationResponse;
import com.duong.chatapplication.service.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversations")
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping
    public ApiResponse<CreateConversationResponse> createConversation(
            @AuthenticationPrincipal Jwt jwt, // Lấy thông tin user từ JWT token
            @RequestBody @Valid CreateConversationRequest request) {
        // Lấy userId từ JWT subject (đã được set khi generate token)
        var creatorId = jwt.getSubject();

        // Gọi service để tạo conversation
        var data = conversationService.createConversation(creatorId, request);

        return ApiResponse.<CreateConversationResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Conversation created successfully")
                .data(data)
                .build();
    }
}
