package az.developia.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import az.developia.shop.entity.SellerEntity;
import az.developia.shop.response.ResponseMessage;
import az.developia.shop.service.SellerService;

@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*")
@RestController
@RequestMapping("/users")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/register")
    public ResponseEntity<Object> addSeller(@RequestBody SellerEntity seller) {
        sellerService.add(seller);
        return ResponseEntity.ok().body(new ResponseMessage("Seller added successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<SellerEntity> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Получаем имя пользователя из контекста безопасности
        SellerEntity seller = sellerService.findByUsername(username);

        if (seller == null) {
            return ResponseEntity.notFound().build();  // Возвращаем 404, если пользователь не найден
        }

        return ResponseEntity.ok(seller);  // Возвращаем информацию о пользователе
    }

}
