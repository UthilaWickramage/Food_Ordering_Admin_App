package lk.software.app.foodorderingadminapp.model;
public class Banner {
    private String bannerId;
    private String documentId;
    private String bannerImage;

    public Banner(String bannerId, String bannerImage) {
        this.bannerId = bannerId;
        this.bannerImage = bannerImage;
    }

    public Banner() {
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}

