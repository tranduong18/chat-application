package com.duong.chatapplication.service;

import com.duong.chatapplication.dto.request.CreateUserRequest;
import com.duong.chatapplication.dto.response.CreateUserResponse;
import com.duong.chatapplication.entity.Role;
import com.duong.chatapplication.entity.User;
import com.duong.chatapplication.exception.AppException;
import com.duong.chatapplication.exception.ErrorCode;
import com.duong.chatapplication.repository.RoleRepository;
import com.duong.chatapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.duong.chatapplication.constant.AppConstant.USER_ROLE;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Role role = roleRepository.findByName(USER_ROLE)
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(USER_ROLE)
                        .build()));
        user.addRole(role);

        userRepository.save(user);

        return CreateUserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
