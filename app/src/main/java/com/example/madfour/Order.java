package com.example.madfour;

import java.util.List;

public class Order {
    private String id;
    private String username;
    private List<String> items;
    private String status;
    private List<Medicine> itemss;
    private String totalAmount;

    public Order(String id, String username, List<String> items, String status) {
        this.id = id;
        this.username = username;
        this.items = items;
        this.status = status;
    }

    public Order(String id, String username, List<String> items, String status, String totalAmount) {
        this.id = id;
        this.username = username;
        this.items = items;
        this.status = status;
        this.totalAmount=totalAmount;
    }

//    public Order(String id, String username, String status, List<Medicine> itemss) {
//        this.id = id;
//        this.username = username;
//        this.itemss = itemss;
//        this.status = status;
//    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getItems() {
        return items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public List<Medicine> getItemss() {
        return itemss;
    }
//    public double getTotalAmount() {
//        double total = 0;
//        for (Medicine item : itemss) {
//            total += Double.parseDouble(item.getPrice().replace("Rs.", ""));
//        }
//        return total;
//    }
}