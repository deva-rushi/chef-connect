package application.models;

import java.util.ArrayList;
import java.util.List;

public class Chef extends User {
    private String cuisine;
    private double rating;
    private List<Integer> ratings;

    public Chef(String username, String password, String cuisine) {
        super(username, password, "Chef");
        this.cuisine = cuisine;
        this.rating = 0.0;
        this.ratings = new ArrayList<>();
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public void addRating(int newRating) {
        ratings.add(newRating);
        calculateAverageRating();
    }

    private void calculateAverageRating() {
        if (ratings.isEmpty()) {
            rating = 0.0;
            return;
        }

        double sum = 0;
        for (int r : ratings) {
            sum += r;
        }
        rating = sum / ratings.size();
    }
}