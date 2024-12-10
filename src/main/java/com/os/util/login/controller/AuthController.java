package com.os.util.login.controller;


import com.os.repository.UserRepository;
import com.os.user.entity.User;
import com.os.user.service.UserService;
import com.os.util.common.CommonResponse;
import com.os.util.enums.UserRole;
import com.os.util.jwt.JwtUtil;
import com.os.util.login.dto.LoginRequest;
import com.os.util.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private UserService userService; // UserService 주입

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          UserService userService) { // UserService 주입
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService; // UserService 주입
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<Map<String, String>>> login(
            @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpSession session) {

        // Authorization 헤더에 토큰이 있는지 확인 (필터에서 이미 인증이 진행됨)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // 토큰이 유효하다고 가정하고 바로 성공 응답을 반환
            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put("accessToken", token);

            CommonResponse<Map<String, String>> response = new CommonResponse<>(
                    HttpStatus.OK.value(), "로그인 성공", tokenResponse);

            return ResponseEntity.ok(response);
        }

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth != null && currentAuth.isAuthenticated() &&
                !currentAuth.getName().equals("anonymousUser")) {
            SecurityContextHolder.clearContext();
            session.invalidate();
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getAccountId(),
                            loginRequest.getPassword())
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (userDetails.getRole() == UserRole.ADMIN) {
                String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername(), userDetails.getRole());
                String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername(), userDetails.getRole());

                Map<String, String> tokenResponse = new HashMap<>();
                tokenResponse.put("accessToken", accessToken);
                tokenResponse.put("refreshToken", refreshToken);
                tokenResponse.put("userId", userDetails.getUserId().toString());
                tokenResponse.put("role", userDetails.getRole().name());

                session.setAttribute("accessToken", accessToken);
                session.setAttribute("refreshToken", refreshToken);

                // 마지막 로그인 시간 업데이트
                userService.updateLastLogin(userDetails.getUsername());

                // ADMIN 권한일 경우 localhost:8081/admin으로 리다이렉트
                tokenResponse.put("redirectUrl", "http://localhost:8081/login");

                return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), "로그인 성공", tokenResponse));
            }

            String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername(), userDetails.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername(), userDetails.getRole());

            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put("accessToken", accessToken);
            tokenResponse.put("refreshToken", refreshToken);
            tokenResponse.put("userId", userDetails.getUserId().toString());
            tokenResponse.put("role", userDetails.getRole().name());

            session.setAttribute("accessToken", accessToken);
            session.setAttribute("refreshToken", refreshToken);

            userService.updateLastLogin(userDetails.getUsername());

            CommonResponse<Map<String, String>> response = new CommonResponse<>(
                    HttpStatus.OK.value(), "로그인 성공", tokenResponse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            CommonResponse<Map<String, String>> response = new CommonResponse<>(
                    HttpStatus.UNAUTHORIZED.value(), "로그인 실패", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            CommonResponse<String> response = new CommonResponse<>(
                    HttpStatus.UNAUTHORIZED.value(), "로그인 먼저 해주세요.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            CommonResponse<String> response = new CommonResponse<>(
                    HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String username = jwtUtil.getUsername(token);
        if (username == null) {
            CommonResponse<String> response = new CommonResponse<>(
                    HttpStatus.UNAUTHORIZED.value(), "로그인되지 않은 상태입니다.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Optional<User> userOptional = userRepository.findByAccountIdAndStatus(username, "ACTIVE");

        if (!userOptional.isPresent()) {
            CommonResponse<String> response = new CommonResponse<>(
                    HttpStatus.UNAUTHORIZED.value(), "이미 로그아웃된 상태입니다.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        SecurityContextHolder.clearContext();
        CommonResponse<String> response = new CommonResponse<>(
                HttpStatus.OK.value(), "로그아웃 성공", "로그아웃이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<Map<String, String>>> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            CommonResponse<Map<String, String>> response = new CommonResponse<>(
                    HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 리프레시 토큰입니다.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String newAccessToken = jwtUtil.refreshToken(refreshToken);
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("accessToken", newAccessToken);

        CommonResponse<Map<String, String>> response = new CommonResponse<>(
                HttpStatus.OK.value(), "새로운 액세스 토큰 발급 성공", tokenResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
        if (jwtUtil.validateToken(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}