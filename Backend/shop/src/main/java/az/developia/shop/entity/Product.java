package az.developia.shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Бренд не должен быть пустым")
    private String brand;

    @NotBlank(message = "Модель не должна быть пустой")
    private String model;

    @NotBlank(message = "Категория не должна быть пустой")
    private String category;

    @Column(length = 1024)
    @Size(max = 1024, message = "Описание не должно превышать 1024 символа")
    private String description;

    @NotNull(message = "Цена не должна быть пустой")
    @Positive(message = "Цена должна быть положительным числом")
    private Double price;

    @NotNull(message = "Рейтинг не должен быть пустым")
    @Min(value = 0, message = "Рейтинг не может быть меньше 0")
    @Max(value = 5, message = "Рейтинг не может быть больше 5")
    private Double rating;

    @Column(name = "image_url")
    @NotBlank(message = "URL изображения не должен быть пустым")
    private String imageUrl;

    @NotNull(message = "ID владельца не должен быть пустым")
    private Long ownerId;
}
