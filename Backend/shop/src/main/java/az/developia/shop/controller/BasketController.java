package az.developia.shop.controller;

import az.developia.shop.entity.BasketItem;
import az.developia.shop.service.BasketService;
import az.developia.shop.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*")
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    private BasketService basketService;
    
    @Autowired
    private JwtUtil jwtUtil;

    private Long getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Получаем username (обычно email)
        return jwtUtil.getUserIdFromUsername(username);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<BasketItem> getBasket() {
        Long userId = getUserIdFromToken();
        return basketService.getBasketItems(userId);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void addToBasket(@RequestParam Long productId, @RequestParam int quantity) {
        Long userId = getUserIdFromToken();
        basketService.addProductToBasket(userId, productId, quantity);
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void clearBasket() {
        Long userId = getUserIdFromToken();
        basketService.clearBasket(userId);
    }

    // Новый API для удаления одного товара из корзины
    @DeleteMapping("/remove/{basketItemId}")  
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void removeBasketItem(@PathVariable Long basketItemId) {
        Long userId = getUserIdFromToken();
        basketService.removeBasketItem(userId, basketItemId);
    }
}
