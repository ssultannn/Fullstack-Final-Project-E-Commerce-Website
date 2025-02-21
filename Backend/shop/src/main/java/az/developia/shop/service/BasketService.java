package az.developia.shop.service;

import az.developia.shop.entity.Basket;
import az.developia.shop.entity.BasketItem;
import az.developia.shop.repository.BasketItemRepository;
import az.developia.shop.repository.BasketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasketService {
	@Autowired
	private BasketRepository basketRepository;
	@Autowired
	private BasketItemRepository basketItemRepository;

	public Basket getBasketByUserId(Long userId) {
		return basketRepository.findByUserId(userId);
	}

	public void addProductToBasket(Long userId, Long productId, int quantity) {
		Basket basket = basketRepository.findByUserId(userId);
		if (basket == null) {
			basket = new Basket();
			basket.setUserId(userId);
			basket = basketRepository.save(basket);
		}

		BasketItem item = new BasketItem();
		item.setBasketId(basket.getId());
		item.setProductId(productId);
		item.setQuantity(quantity);
		basketItemRepository.save(item);
	}

	public List<BasketItem> getBasketItems(Long userId) {
		Basket basket = basketRepository.findByUserId(userId);
		return basket != null ? basketItemRepository.findByBasketId(basket.getId()) : List.of();
	}

	public void clearBasket(Long userId) {
		Basket basket = basketRepository.findByUserId(userId);
		if (basket != null) {
			basketItemRepository.deleteAll(basketItemRepository.findByBasketId(basket.getId()));
		}
	}

	   public void removeBasketItem(Long userId, Long basketItemId) {
	        Basket basket = basketRepository.findByUserId(userId);
	        if (basket == null) {
	            throw new RuntimeException("Basket not found for user " + userId);
	        }
	        Optional<BasketItem> basketItemOpt = basketItemRepository.findById(basketItemId);
	        if (basketItemOpt.isEmpty()) {
	            throw new RuntimeException("Basket item not found");
	        }
	        BasketItem basketItem = basketItemOpt.get();
	        if (!basket.getId().equals(basketItem.getBasketId())) {
	            throw new RuntimeException("Basket item does not belong to the user");
	        }
	        basketItemRepository.delete(basketItem);
	    }
}
