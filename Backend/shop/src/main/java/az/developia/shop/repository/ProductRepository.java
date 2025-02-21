package az.developia.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import az.developia.shop.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByOwnerId(Long ownerId);
    
   List<Product> findByCategory(String category);
    
    List<Product> findByBrandContainingIgnoreCase(String brand);
    
    List<Product> findByRatingGreaterThanEqual(double rating);
    
    List<Product> findAllByOrderByPriceAsc();
    
    List<Product> findAllByOrderByPriceDesc();
}