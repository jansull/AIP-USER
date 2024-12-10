package com.os.util.security;

import com.os.user.entity.User;
import com.os.util.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserDetailsImpl implements UserDetails {

  private final String accountId;
  private final String accountPw;
  private final UserRole role;
  private final User user;

  public UserDetailsImpl(User user) {
    this.accountId = user.getAccountId();
    this.accountPw = user.getAccountPw();
    this.role = user.getRole();
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }

  @Override
  public String getPassword() {
    return accountPw;
  }

  @Override
  public String getUsername() {
    return accountId;
  }

  public UserRole getRole() {
    return role;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Long getUserId() {
    if (user != null) {
      return user.getId();
    } else {
      throw new IllegalStateException("존재하지 않는 사용자 입니다.");
    }
  }
}