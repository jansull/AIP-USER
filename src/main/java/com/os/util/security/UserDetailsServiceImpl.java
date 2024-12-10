package com.os.util.security;


import com.os.repository.UserRepository;
import com.os.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements
    org.springframework.security.core.userdetails.UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findByAccountId(accountId);
    if (userOptional.isPresent()) {
      return new UserDetailsImpl(userOptional.get());
    }

    throw new UsernameNotFoundException(
        "사용자를 찾을 수 없습니다. 아이디: " + accountId);
  }

}