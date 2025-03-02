package com.example.madfour;

public class MedicineAdmin {
    private String id;
    private String name;
    private String description;
    private String category;
    private String price;
    private String imageUrl;

    public MedicineAdmin(String id, String name, String description, String category, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public MedicineAdmin(String id, String name, String description, String category, String imageUrl, String price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.price=price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }
}