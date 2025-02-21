package az.developia.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import az.developia.shop.entity.Product;
import az.developia.shop.repository.ProductRepository;

@Service
public class ProductService {
    
    
    @Autowired
    private  ProductRepository productRepository;

    
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
    
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public List<Product> getProductsByOwnerId(Long ownerId) {
        return productRepository.findByOwnerId(ownerId);
    }
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByBrandContainingIgnoreCase(keyword);
    }
    
    public List<Product> getProductsByRating(double rating) {
        return productRepository.findByRatingGreaterThanEqual(rating);
    }
    
    public List<Product> sortProductsByPrice(String order) {
        if (order.equalsIgnoreCase("asc")) {
            return productRepository.findAllByOrderByPriceAsc();
        } else {
            return productRepository.findAllByOrderByPriceDesc();
        }
    }
}