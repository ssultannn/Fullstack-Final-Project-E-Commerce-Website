// Загружаем header
function loadHeader() {
  fetch("header.html")
    .then((response) => response.text())
    .then((data) => {
      document.body.insertAdjacentHTML("afterbegin", data);
      const username = localStorage.getItem("username");
      if (username) {
        setUsername(username);
      } else {
        const usernameElement = document.getElementById("username-placeholder");
        if (usernameElement) {
          usernameElement.textContent = "Guest";
        }
      }
    })
    .catch((error) => console.error("Ошибка загрузки header:", error));
}

function setUsername(username) {
  if (!username) return;
  localStorage.setItem("username", username);
  const usernameElement = document.getElementById("username-placeholder");
  if (usernameElement) {
    usernameElement.textContent = username;
  } else {
    console.warn("Элемент username-placeholder не найден!");
  }
}

function logout() {
  // Удаляем JWT из localStorage
  localStorage.removeItem("jwt");

  // Сбрасываем имя пользователя
  const usernameElement = document.getElementById("username-placeholder");
  if (usernameElement) {
    usernameElement.textContent = "username";
  }

  // Скрываем кнопку "Log out", если она есть
  const logoutButton = document.querySelector(".logout");
  if (logoutButton) {
    logoutButton.style.display = "none";
  }

  // Переадресация на страницу входа или главную страницу
  window.location.href = "/index.html";
}

// Загружаем header при загрузке страницы
document.addEventListener("DOMContentLoaded", loadHeader);

document.addEventListener("DOMContentLoaded", function () {
  const productsContainer = document.getElementById("productsContainer");
  const searchInput = document.getElementById("searchInput");
  const categoryList = document.querySelectorAll(".filter-container ul li");
  const ratingStars = document.querySelectorAll(".ratings span");

  // Получение всех товаров с сервера
  async function fetchProducts() {
    try {
      const response = await fetch("http://localhost:8080/products");
      const products = await response.json();
      displayProducts(products);
    } catch (error) {
      console.error("Error loading products:", error);
    }
  }

  // Отображение товаров на странице
  function displayProducts(products) {
    productsContainer.innerHTML = ""; // Очищаем контейнер

    products.forEach((product) => {
      const productElement = document.createElement("div");
      productElement.classList.add("product");

      productElement.innerHTML = `
        <img src="${product.imageUrl}" alt="Product Image">
        <!-- Оборачиваем название товара в ссылку, ведущую на product.html?id=... -->
        <h3>
          <a href="product.html?id=${product.id}">
            ${product.brand} ${product.model}
          </a>
        </h3>
        <p>${product.price}</p>
        <div class="rating">${getRatingStars(product.rating)} (${
        product.rating
      })</div>
        <button class="add-to-basket" data-id="${
          product.id
        }">Add to Basket</button>
      `;

      productsContainer.appendChild(productElement);
    });

    // Назначаем обработчик для кнопок "Add to Basket"
    const addButtons = productsContainer.querySelectorAll(".add-to-basket");
    addButtons.forEach((button) => {
      button.addEventListener("click", function () {
        const productId = this.getAttribute("data-id");
        addToBasket(productId);
      });
    });
  }

  // Функция добавления товара в корзину (обращается к бэкенду)
  async function addToBasket(productId) {
    const token = localStorage.getItem("jwt");
    if (!token) {
      alert("Пользователь не авторизован. Пожалуйста, войдите в систему.");
      return;
    }
    try {
      const response = await fetch(
        `http://localhost:8080/basket/add?productId=${productId}&quantity=1`,
        {
          method: "POST",
          headers: {
            Authorization: "Bearer " + token,
            "Content-Type": "application/json",
          },
        }
      );
      if (response.ok) {
        alert("Товар успешно добавлен в корзину!");
      } else {
        const errorText = await response.text();
        console.error("Ошибка при добавлении товара в корзину:", errorText);
        alert("Ошибка при добавлении товара в корзину.");
      }
    } catch (error) {
      console.error("Ошибка сети:", error);
      alert("Ошибка сети при добавлении товара в корзину.");
    }
  }

  // Преобразование рейтинга в звёзды
  function getRatingStars(rating) {
    const fullStars = Math.floor(rating);
    const halfStar = rating % 1 !== 0;
    let stars = "★".repeat(fullStars);
    if (halfStar) stars += "☆";
    return stars.padEnd(5, "☆");
  }

  // Функционал поиска
  searchInput.addEventListener("input", function () {
    const searchTerm = searchInput.value.toLowerCase();
    const products = document.querySelectorAll(".product");
    products.forEach((product) => {
      const productName = product.querySelector("h3").textContent.toLowerCase();
      product.style.display = productName.includes(searchTerm)
        ? "block"
        : "none";
    });
  });

  // Фильтрация по категориям
  categoryList.forEach((category) => {
    category.addEventListener("click", function () {
      const categoryName = category.getAttribute("data-category");
      fetchProductsByCategory(categoryName);
    });
  });

  // Фильтрация по рейтингу
  ratingStars.forEach((star) => {
    star.addEventListener("click", function () {
      const rating = star.getAttribute("data-rating");
      fetchProductsByRating(rating);
    });
  });

  // Получение товаров по категории
  async function fetchProductsByCategory(category) {
    try {
      const response = await fetch(
        `http://localhost:8080/products/category?category=${category}`
      );
      const products = await response.json();
      displayProducts(products);
    } catch (error) {
      console.error("Error filtering products by category:", error);
    }
  }

  // Получение товаров по рейтингу
  async function fetchProductsByRating(rating) {
    try {
      const response = await fetch(
        `http://localhost:8080/products/rating?rating=${rating}`
      );
      const products = await response.json();
      displayProducts(products);
    } catch (error) {
      console.error("Error filtering products by rating:", error);
    }
  }

  // Обработчик для кнопки "All Products"
  document
    .getElementById("allProductsBtn")
    .addEventListener("click", fetchProducts);

  // Сортировка товаров
  document.getElementById("sortSelect").addEventListener("change", function () {
    const value = this.value;
    if (value) {
      const [key, order] = value.split("_");
      fetchProductsBySort(key, order);
    }
  });

  async function fetchProductsBySort(key, order) {
    try {
      const response = await fetch(
        `http://localhost:8080/products/price?order=${order}`
      );
      const products = await response.json();
      displayProducts(products);
    } catch (error) {
      console.error("Error sorting products:", error);
    }
  }

  // Начальная загрузка всех товаров
  fetchProducts();
});

function loadFooter() {
  fetch("footer.html")
    .then((response) => response.text())
    .then((data) => {
      document.body.insertAdjacentHTML("beforeend", data);
    })
    .catch((error) => console.error("Ошибка загрузки footer:", error));
}
loadFooter();
