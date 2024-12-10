package com.os.util.login.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

  private String accountId;

  private String password;
}