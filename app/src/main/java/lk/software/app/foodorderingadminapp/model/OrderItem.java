package lk.software.app.foodorderingadminapp.model;

public class OrderItem {
    private String product_name;
    private double price;
    private int quantity;

    private String image;

    public OrderItem(String product_name, double price, int quantity,String image) {
        this.product_name = product_name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public OrderItem() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
