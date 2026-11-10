package com.duong.chatapplication.service;

import com.duong.chatapplication.common.ConversationType;
import com.duong.chatapplication.dto.request.CreateConversationRequest;
import com.duong.chatapplication.dto.response.CreateConversationResponse;
import com.duong.chatapplication.entity.Conversation;
import com.duong.chatapplication.entity.User;
import com.duong.chatapplication.exception.AppException;
import com.duong.chatapplication.exception.ErrorCode;
import com.duong.chatapplication.mapper.ConversationMapper;
import com.duong.chatapplication.repository.ConversationRepository;
import com.duong.chatapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CONVERSATION-SERVICE")
public class ConversationService {
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;

    public CreateConversationResponse createConversation(String creatorId, CreateConversationRequest request){
        List<String> participantIds = request.participantIds();

        // Đảm bảo creator cũng nằm trong danh sách participants
        if(!participantIds.contains(creatorId)){
            participantIds.add(creatorId);
        }

        // Lấy thông tin tất cả participants từ database
        List<User> participantInfos = userRepository.findAllById(participantIds);

        // Kiểm tra xem tất cả participants có tồn tại không
        if(participantInfos.size() != participantIds.size()){
            throw new AppException(ErrorCode.PARTICIPANT_NOT_FOUND);
        }

        ConversationType conversationType = request.conversationType();
        String participantHash = null;

        // Xử lý logic cho PRIVATE conversation
        if(conversationType == ConversationType.PRIVATE){
            // Private conversation phải có đúng 2 người
            if(participantInfos.size() != 2){
                throw new AppException(ErrorCode.INVALID_PARTICIPANT_COUNT);
            }

            // Tạo participant hash để identify unique conversation
            // Sort userId để đảm bảo hash luôn giống nhau cho cùng 2 người
            // Ví dụ: userId1="abc", userId2="xyz" -> hash="abc_xyz"
            participantHash = participantInfos.stream()
                    .map(User::getId).sorted()
                    .collect(Collectors.joining("_"));

            // Kiểm tra xem conversation giữa 2 người này đã tồn tại chưa
            Optional<Conversation> existingConversation = conversationRepository.findByParticipantHash(participantHash);
            if(existingConversation.isPresent()){
                return ConversationMapper.toConversationResponse(creatorId, existingConversation.get());
            }
        }

        // Xử lý logic cho GROUP conversation
        if(conversationType == ConversationType.GROUP){
            // Group conversation phải có tên
            if(request.name() == null || request.name().trim().isEmpty()){
                throw new AppException(ErrorCode.CONVERSATION_NAME_REQUIRED);
            }

            // Group conversation phải có ít nhất 3 người
            if(participantIds.size() < 3){
                throw new AppException(ErrorCode.GROUP_CONVERSATION_MINIMUM_THREE_PARTICIPANTS);
            }
        }

        // Tạo conversation mới
        Conversation conversation = Conversation.builder()
                .name(request.name())
                .conversationType(conversationType)
                .conversationAvatar(request.conversationAvatar())
                .participantHash(participantHash) // Chỉ có giá trị với PRIVATE
                .build();

        // Thêm tất cả participants vào conversation
        participantInfos.forEach(conversation::addParticipant);
        conversationRepository.save(conversation);

        return ConversationMapper.toConversationResponse(creatorId, conversation);
    }
}
