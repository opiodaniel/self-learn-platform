package com.e_learning;

import com.e_learning.model.Role;
import com.e_learning.model.User;
import com.e_learning.repository.UserRepository;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;
import java.util.Base64;

@SpringBootApplication
public class ELearningApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(ELearningApplication.class, args);
//		SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
//		String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
//		System.out.println("Generated Secret Key: " + base64Key);
	}

	public void run(String... args){
		User adminAccount = userRepository.findByRole(Role.ADMIN);
		if(null==adminAccount){
			User user = new User();
			user.setUsername("admin");
			user.setEmail("danielopio540@gmail.com");
			user.setFirstName("opio");
			user.setLastName("daniel");
			user.setRole(Role.ADMIN);
			user.setActive(1);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
		}

	}

}
