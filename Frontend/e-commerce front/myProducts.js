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
  // Удаляем JWT из localStorage
  localStorage.removeItem("jwt");

  // Обновляем интерфейс, например, сбрасываем имя пользователя
  const usernameElement = document.getElementById("username-placeholder");
  if (usernameElement) {
    usernameElement.textContent = "username"; // Сбросить имя на дефолтное
  }

  // Скрываем кнопку "Log out" (если она есть)
  const logoutButton = document.querySelector(".logout");
  if (logoutButton) {
    logoutButton.style.display = "none";
  }

  // Переадресуем на страницу входа или главную страницу
  window.location.href = "/index.html"; // Например, переадресуем на страницу входа
}

// Вызываем загрузку хедера при загрузке страницы
document.addEventListener("DOMContentLoaded", loadHeader);
const token = localStorage.getItem("jwt");
const API_BASE = "http://localhost:8080/user/products";

async function fetchProducts() {
  try {
    const response = await fetch(API_BASE, {
      method: "GET",
      headers: { Authorization: "Bearer " + token },
    });
    if (!response.ok) {
      throw new Error("Error fetching products");
    }
    const products = await response.json();
    populateTable(products);
  } catch (error) {
    console.error("Error:", error);
  }
}

function populateTable(products) {
  const tbody = document.querySelector("#productsTable");
  tbody.innerHTML = "";
  products.forEach((product) => {
    const row = document.createElement("tr");
    row.innerHTML = `
            <td>${product.id}</td>
            <td>${product.brand}</td>
            <td>${product.model}</td>
            <td>${product.category}</td>
            <td>${product.price}</td>
            <td>${product.rating}</td>
            <td><img src="${product.imageUrl}" alt="Image"></td>
            <td>
                <button class="btn btn-warning btn-sm" onclick='editProduct(${JSON.stringify(
                  product
                )})'>Edit</button>
                <button class="btn btn-danger btn-sm" onclick='deleteProduct(${
                  product.id
                })'>Delete</button>
            </td>
        `;
    tbody.appendChild(row);
  });
}

function showModal() {
  resetForm();
  const modal = new bootstrap.Modal(document.getElementById("productModal"));
  modal.show();
}

function editProduct(product) {
  document.getElementById("productId").value = product.id;
  document.getElementById("brand").value = product.brand;
  document.getElementById("model").value = product.model;
  document.getElementById("category").value = product.category;
  document.getElementById("description").value = product.description;
  document.getElementById("price").value = product.price;
  document.getElementById("rating").value = product.rating;
  document.getElementById("imageUrl").value = product.imageUrl;
  const modal = new bootstrap.Modal(document.getElementById("productModal"));
  modal.show();
}

async function saveProduct() {
  const id = document.getElementById("productId").value;
  const product = {
    brand: document.getElementById("brand").value,
    model: document.getElementById("model").value,
    category: document.getElementById("category").value,
    description: document.getElementById("description").value,
    price: parseFloat(document.getElementById("price").value),
    rating: parseFloat(document.getElementById("rating").value),
    imageUrl: document.getElementById("imageUrl").value,
  };

  try {
    if (id) {
      const response = await fetch(`${API_BASE}/update/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
        body: JSON.stringify(product),
      });
      if (!response.ok) throw new Error("Error updating product");
    } else {
      const response = await fetch(`${API_BASE}/add`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
        body: JSON.stringify(product),
      });
      if (!response.ok) throw new Error("Error adding product");
    }
    const modal = bootstrap.Modal.getInstance(
      document.getElementById("productModal")
    );
    modal.hide();
    fetchProducts();
  } catch (error) {
    console.error("Error:", error);
  }
}

async function deleteProduct(id) {
  if (!confirm("Are you sure you want to delete this product?")) return;
  try {
    const response = await fetch(`${API_BASE}/delete/${id}`, {
      method: "DELETE",
      headers: { Authorization: "Bearer " + token },
    });
    if (!response.ok) throw new Error("Error deleting product");
    fetchProducts();
  } catch (error) {
    console.error("Error:", error);
  }
}

function resetForm() {
  document.getElementById("productForm").reset();
  document.getElementById("productId").value = "";
}

fetchProducts();
document.addEventListener("DOMContentLoaded", function () {
  loadFooter();
});

function loadFooter() {
  fetch("footer.html")
    .then((response) => response.text())
    .then((data) => {
      const footerContainer = document.createElement("div");
      footerContainer.innerHTML = data;
      document.body.appendChild(footerContainer);
    })
    .catch((error) => console.error("Ошибка загрузки footer:", error));
}
