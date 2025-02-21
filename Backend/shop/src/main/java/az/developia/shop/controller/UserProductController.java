package az.developia.shop.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import az.developia.shop.entity.Product;
import az.developia.shop.entity.UserEntity;
import az.developia.shop.service.ProductService;
import az.developia.shop.service.UserService;
@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*")

@RestController
@RequestMapping("/user/products")

public class UserProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;

    // Получение списка продуктов текущего пользователя

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Product> getUserProducts(Authentication authentication) {
        String username = authentication.getName();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return productService.getProductsByOwnerId(user.getId());
    }

    // Добавление нового продукта
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Product addProduct(@RequestBody Product product, Authentication authentication) {
        String username = authentication.getName();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        product.setOwnerId(user.getId());
        return productService.addProduct(product);
    }
    
    // Обновление продукта
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestBody Product productDetails,
                                 Authentication authentication) {
        String username = authentication.getName();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productService.getProductById(id);
        if (!product.getOwnerId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: you can update only your own products");
        }
        product.setBrand(productDetails.getBrand());
        product.setModel(productDetails.getModel());
        product.setCategory(productDetails.getCategory());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setRating(productDetails.getRating());
        product.setImageUrl(productDetails.getImageUrl());
        return productService.updateProduct(product);
    }
    
    // Удаление продукта
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String deleteProduct(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productService.getProductById(id);
        if (!product.getOwnerId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: you can delete only your own products");
        }
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
}

