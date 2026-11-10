package com.duong.chatapplication.controller;

import com.duong.chatapplication.dto.request.CreateConversationRequest;
import com.duong.chatapplication.dto.response.ApiResponse;
import com.duong.chatapplication.dto.response.ConversationDetailResponse;
import com.duong.chatapplication.dto.response.CreateConversationResponse;
import com.duong.chatapplication.dto.response.PageResponse;
import com.duong.chatapplication.service.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/my-conversation")
    public ApiResponse<PageResponse<ConversationDetailResponse>> getMyConversation(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ){
        String userId = jwt.getSubject();
        var data = conversationService.getMyConversation(userId, page, size);

        return ApiResponse.<PageResponse<ConversationDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get my conversation successfully")
                .data(data)
                .build();
    }
}
