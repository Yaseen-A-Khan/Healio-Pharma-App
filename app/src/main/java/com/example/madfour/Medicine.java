package com.example.madfour;

public class Medicine {
    private String name;
    private String description;
    private String category;
    private String imageUrl;
    private String price;

    public Medicine(String name, String description, String category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public Medicine(String name, String description, String category, String imageUrl, String price) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.price=price;
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

