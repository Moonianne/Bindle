package com.android.group.model.yelp;

public final class Business {
    private String name;
    private String image_url;
    private double rating;
    private String price;
    private String phone;

    public Business(final String name,
                    final String image_url,
                    final double rating,
                    final String price,
                    final String phone) {
        this.name = name;
        this.image_url = image_url;
        this.rating = rating;
        this.price = price;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }

    public double getRating() {
        return rating;
    }

    public String getPrice() {
        return price;
    }

    public String getPhone() {
        return phone;
    }
}
