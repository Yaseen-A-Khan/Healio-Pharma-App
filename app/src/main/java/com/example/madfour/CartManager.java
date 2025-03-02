package com.example.madfour;

import java.util.HashMap;
import java.util.Map;

public class CartManager {
    private static CartManager instance;
    private Map<Medicine, Integer> cartItems;

    private CartManager() {
        cartItems = new HashMap<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Medicine medicine) {
        if (cartItems.containsKey(medicine)) {
            cartItems.put(medicine, cartItems.get(medicine) + 1);
        } else {
            cartItems.put(medicine, 1);
        }
    }

    public void removeFromCart(Medicine medicine) {
        if (cartItems.containsKey(medicine)) {
            int quantity = cartItems.get(medicine);
            if (quantity > 1) {
                cartItems.put(medicine, quantity - 1);
            } else {
                cartItems.remove(medicine);
            }
        }
    }

    public Map<Medicine, Integer> getCartItems() {
        return new HashMap<>(cartItems);
    }

    public void clearCart() {
        cartItems.clear();
    }

//    public double getTotalAmount() {
//        double total = 0;
//        for (Map.Entry<Medicine, Integer> entry : cartItems.entrySet()) {
//            total += Double.parseDouble(entry.getKey().getPrice().replace("Rs.", "")) * entry.getValue();
//        }
//        return total;
//    }
}