package com.os.util.security;


import com.os.util.enums.UserRole;
import com.os.util.jwt.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

  private final RedisTemplate<String, String> redisTemplate;
  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsServiceImpl;

  public RefreshTokenService(RedisTemplate<String, String> redisTemplate, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
    this.redisTemplate = redisTemplate;
    this.jwtUtil = jwtUtil;
    this.userDetailsServiceImpl = userDetailsServiceImpl;
  }

  /**
   * 리프레시 토큰 저장
   *
   * @param username 사용자 이름
   * @return 저장된 리프레시 토큰
   */
  public String createRefreshToken(String username) {
    // 사용자 권한 정보 가져오기
    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
    UserRole role = userDetails.getAuthorities().stream()
            .map(authority -> UserRole.valueOf(authority.getAuthority()))
            .findFirst()
            .orElse(UserRole.ADMIN);

    // 권한 정보를 포함하여 리프레시 토큰 생성
    String token = jwtUtil.generateRefreshToken(username, role);
    redisTemplate.opsForValue()
            .set(username, token, jwtUtil.getRefreshTokenValidity(), TimeUnit.MILLISECONDS);
    return token;
  }

  /**
   * 리프레시 토큰 삭제
   *
   * @param username 사용자 이름
   */
  public void deleteByUsername(String username) {
    redisTemplate.delete(username);
  }

  /**
   * 리프레시 토큰 검증
   *
   * @param token 검증할 리프레시 토큰
   * @return 유효한 토큰인지 여부
   */
  public boolean isRefreshTokenValid(String token) {
    String username = jwtUtil.getUsername(token);
    String storedToken = redisTemplate.opsForValue().get(username);
    return token.equals(storedToken) && jwtUtil.validateToken(token);
  }
}