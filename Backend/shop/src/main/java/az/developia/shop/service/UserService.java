package az.developia.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import az.developia.shop.entity.SellerEntity;
import az.developia.shop.entity.UserEntity;
import az.developia.shop.repository.SellerRepository;
import az.developia.shop.repository.UserRepository;
import jakarta.annotation.PostConstruct;

import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
	private UserRepository repository;

	public void addSeller(SellerEntity req) {
		UserEntity user = new UserEntity();
		user.setUsername(req.getUsername());

		user.setPassword("{noop}" + req.getPassword());
		user.setEnabled(1);
		repository.save(user);

	}

	public Optional<UserEntity> findByUsername(String username) {
		return repository.findByUsername(username);
	}

}
