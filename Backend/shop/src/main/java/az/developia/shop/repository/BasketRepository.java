package az.developia.shop.repository;

import az.developia.shop.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findByUserId(Long userId);
}
