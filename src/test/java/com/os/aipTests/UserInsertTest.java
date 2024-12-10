package com.os.aipTests;

import com.os.user.entity.User;
import com.os.repository.UserRepository;
import com.os.util.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
@SpringBootTest
public class UserInsertTest {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;



	@Test
	void insert(){
		User user = User.builder()
				.accountId("asdasd.com")
				.accountPw(passwordEncoder.encode(	"1234"))
				.username("kim")
				.role(UserRole.ADMIN)
				.build();
		userRepository.save(user);
	}
}
