package com.os.user.service;

import com.os.repository.UserRepository;
import com.os.user.dto.UserResponse;
import com.os.user.entity.User;
import com.os.util.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     @method : UserService
     @desc : 생성자주입
     @author : 김홍성
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     @method : getUserById
     @desc : 주어진 사용자 ID를 사용하여 사용자를 찾는 메서드
     @author : 김홍성
     */
    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    /**
     @method : updatePassword
     @desc : 새로운 비밀번호로 변경하는 메서드
     @author : 김홍성
     */
    public void updatePassword(Long userId, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            User updatedUser = User.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .accountId(user.getAccountId())
                    .role(user.getRole())
                    .accountPw(passwordEncoder.encode(newPassword))
                    .build();

            userRepository.save(updatedUser);
        });
    }

    /**
     @method : getUserResponseById
     @desc : 주어진 사용자 ID를 사용하여 사용자를 찾고 userResponse 에 등록하는 메서드
     @author : 김홍성
     */
    public UserResponse getUserResponseById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setAccountId(user.getAccountId());
            userResponse.setAccountPw(user.getAccountPw());
            userResponse.setRole(user.getRole());
            return userResponse;
        } else {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
    }

    /**
        @method : findId
        @desc : 시큐리티에서 user 정보를 가져오는 메서드
        @author : 김성민
    */

    public User findId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.orElse(null);
    }

    public void updateLastLogin(String accountId) {
        Optional<User> optionalUser = userRepository.findByAccountId(accountId);
        optionalUser.ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }
}