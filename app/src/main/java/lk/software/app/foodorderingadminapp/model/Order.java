package lk.software.app.foodorderingadminapp.model;

public class Order {
    private String documentId;
    private String customer_name;
    private double totalOrderPrice;
    private String currentSaveDate;
    private  String orderStatus;

    public String customer_id;

    public Order() {
    }

    public Order(String documentId, String customer_name, double totalOrderPrice, String currentSaveDate, String orderStatus) {
        this.documentId = documentId;
        this.customer_name = customer_name;
        this.totalOrderPrice = totalOrderPrice;
        this.currentSaveDate = currentSaveDate;
        this.orderStatus = orderStatus;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public String getCurrentSaveDate() {
        return currentSaveDate;
    }

    public void setCurrentSaveDate(String currentSaveDate) {
        this.currentSaveDate = currentSaveDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}

