package com.android.group.model.yelp;

public final class Business {
    private String name;
    private String image_url;
    private double rating;
    private String phone;
    private boolean is_closed;

    public Business(final String name,
                    final String image_url,
                    final double rating,
                    final String phone,
                    final boolean is_closed) {
        this.name = name;
        this.image_url = image_url;
        this.rating = rating;
        this.phone = phone;
        this.is_closed = is_closed;
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

    public String getPhone() {
        return phone;
    }

    public boolean getIsClosed() {
        return is_closed;
    }
}
