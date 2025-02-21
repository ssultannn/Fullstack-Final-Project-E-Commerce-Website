package az.developia.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import az.developia.shop.entity.AuthorityEntity;
import az.developia.shop.entity.SellerEntity;
import az.developia.shop.repository.SellerRepository;

@Service
public class SellerService {

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthorityService authorityService;

	public void add(SellerEntity seller) {
		// Сохраняем продавца в таблице sellers
		sellerRepository.save(seller);
		// Создаем запись в таблице users
		userService.addSeller(seller);
		// Добавляем роль в таблицу authorities
		authorityService.addSellerAuthorities(seller);
	}

	 public SellerEntity findByUsername(String username) {
	        return sellerRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
	    }
}
