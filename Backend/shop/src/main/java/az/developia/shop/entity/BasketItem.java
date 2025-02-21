package az.developia.shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "basket_items")
@Data
@NoArgsConstructor
public class BasketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "basket_id", nullable = false)
    private Long basketId; // ID корзины

    @Column(name = "product_id", nullable = false)
    private Long productId; // ID товара

    private int quantity; // Количество товара
}
