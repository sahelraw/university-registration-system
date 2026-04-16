package com.uni.demo;

import com.uni.demo.security.AppUser;
import com.uni.demo.security.AppUserRole;
import com.uni.demo.security.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeAdmin(AppUserRepository repository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (repository.count() == 0) {
				AppUser admin = new AppUser("admin", passwordEncoder.encode("password123"), AppUserRole.ADMIN);
				repository.save(admin);
			}
		};
	}
}
