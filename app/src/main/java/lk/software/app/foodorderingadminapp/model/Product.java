package lk.software.app.foodorderingadminapp.model;

import java.util.ArrayList;
import java.util.List;

import lk.software.app.foodorderingadminapp.R;

public class Product {
    public int id;
    public String name;
    public double price;
    public String category_name;
    public int prepare_time;
    public int rating;
    public String description;
    private int person_per_serve;
    public String image;

    public Product(String name, double price, int prepare_time, int rating, String description,String category_name,int person_per_serve, String image) {
        this.name = name;
        this.price = price;
        this.prepare_time = prepare_time;
        this.rating = rating;
        this.description = description;
        this.category_name = category_name;
        this.person_per_serve = person_per_serve;
        this.image = image;
    }



    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public int getPrepare_time() {
        return prepare_time;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getPerson_per_serve() {
        return person_per_serve;
    }

    public void setPerson_per_serve(int person_per_serve) {
        this.person_per_serve = person_per_serve;
    }

    public void setPrepare_time(int prepare_time) {
        this.prepare_time = prepare_time;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static List<Product> getNewProducts(){
        List<Product> newProducts = new ArrayList<>();

//        newProducts.add(new Product(1,"Chicken Burger",7.99, R.mipmap.burger));
//        newProducts.add(new Product(2,"Italian Pasta",15.99, R.mipmap.pasta));
//        newProducts.add(new Product(3,"Cuttlefish Pizza",5.99, R.mipmap.pizza));
//        newProducts.add(new Product(4,"Chocolate Cake",2.99, R.mipmap.cake));
//        newProducts.add(new Product(4,"Chocolate Cake",2.99, R.mipmap.cake));
//        newProducts.add(new Product(4,"Chocolate Cake",2.99, R.mipmap.cake));
//        newProducts.add(new Product(4,"Chocolate Cake",2.99, R.mipmap.cake));
//        newProducts.add(new Product(4,"Chocolate Cake",2.99, R.mipmap.cake));
//        newProducts.add(new Product(5,"Chicken Wings",7.99, R.mipmap.pizza));

        return newProducts;
    }
}
