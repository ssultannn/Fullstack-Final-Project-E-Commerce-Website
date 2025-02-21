package az.developia.shop.repository;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.developia.shop.entity.SellerEntity;

public interface SellerRepository extends JpaRepository<SellerEntity, Long>{

	Optional<SellerEntity> findByUsername(String username);

	boolean existsByUsername(String username);

}
