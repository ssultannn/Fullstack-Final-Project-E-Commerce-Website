package az.developia.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import az.developia.shop.entity.AuthorityEntity;
import jakarta.transaction.Transactional;

@Transactional
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Integer> {

	@Query(value = "insert into authorities (username,authority) select ?1,authority from authority_list where seller=1", nativeQuery = true)
	@Modifying
	void addSellerAuthorities(String username);



}