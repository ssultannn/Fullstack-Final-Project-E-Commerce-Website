package az.developia.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import az.developia.shop.entity.AuthorityEntity;
import az.developia.shop.entity.SellerEntity;
import az.developia.shop.repository.AuthorityRepository;

@Service
public class AuthorityService {
	@Autowired
	private AuthorityRepository repository;

    public void addSellerAuthorities(SellerEntity seller) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setUsername(seller.getUsername());
        authorityEntity.setAuthority("ROLE_USER");
        repository.save(authorityEntity);
    }
}