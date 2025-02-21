package az.developia.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import az.developia.shop.entity.Product;
import az.developia.shop.service.ProductService;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*")
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    
    // Фильтрация по категории
    @GetMapping("/category")
    public List<Product> getProductsByCategory(@RequestParam String category) {
        return productService.getProductsByCategory(category);
    }
    
    // Поиск продуктов
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }
    
    // Фильтрация по рейтингу
    @GetMapping("/rating")
    public List<Product> filterByRating(@RequestParam double rating) {
        return productService.getProductsByRating(rating);
    }
    
    // Сортировка по цене (возрастание или убывание)
    @GetMapping("/price")
    public List<Product> sortProductsByPrice(@RequestParam String order) {
        return productService.sortProductsByPrice(order);
    }
}
