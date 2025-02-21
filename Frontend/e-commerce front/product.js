function loadHeader() {
  fetch("header.html")
    .then((response) => response.text())
    .then((data) => {
      document.body.insertAdjacentHTML("afterbegin", data);
      const username = localStorage.getItem("username");

      if (username) {
        setUsername(username);
      } else {
        // Handle case where user is not logged in (set a default message)
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
  // Remove JWT and username from localStorage
  localStorage.removeItem("jwt");
  localStorage.removeItem("username");

  // Reset UI elements to show default state (no username)
  const usernameElement = document.getElementById("username-placeholder");
  if (usernameElement) {
    usernameElement.textContent = "Guest"; // Reset to default text
  }

  // Hide the logout button
  const logoutButton = document.querySelector(".logout-btn");
  if (logoutButton) {
    logoutButton.style.display = "none";
  }

  // Redirect to the login page or home page
  window.location.href = "/index.html";
}

// Вызываем загрузку хедера при загрузке страницы
document.addEventListener("DOMContentLoaded", loadHeader);
document.addEventListener("DOMContentLoaded", async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const productId = urlParams.get("id");

  if (!productId) {
    alert("Продукт не найден!");
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/products/${productId}`);
    if (!response.ok) {
      throw new Error("Ошибка при загрузке продукта");
    }

    const product = await response.json();

    document.getElementById("product-name").textContent =
      product.brand + " " + product.model;
    document.getElementById("product-image").src =
      product.imageUrl || "default.jpg";
    document.getElementById("product-description").textContent =
      product.description || "No description";
    document.getElementById("product-price").textContent = `$${product.price}`;
    document.getElementById("product-rating").textContent = getRatingStars(
      product.rating
    );
    document.getElementById("product-reviews").textContent = `(${
      product.reviews || 0
    } Reviews)`;
    document.getElementById("product-stock").textContent = product.inStock
      ? "In Stock"
      : "Out of Stock";

    document
      .getElementById("add-to-cart")
      .addEventListener("click", () => addToBasket(product.id));
  } catch (error) {
    console.error("Ошибка при получении данных о продукте:", error);
    alert("Не удалось загрузить данные о продукте.");
  }
});

function getRatingStars(rating) {
  const fullStars = Math.floor(rating);
  const halfStar = rating % 1 !== 0;
  let stars = "★".repeat(fullStars);
  if (halfStar) stars += "☆";
  return stars.padEnd(5, "☆");
}

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

function loadFooter() {
  fetch("footer.html")
    .then((response) => response.text())
    .then((data) => {
      document.body.insertAdjacentHTML("beforeend", data);
    })
    .catch((error) => console.error("Ошибка загрузки footer:", error));
}
loadFooter();
