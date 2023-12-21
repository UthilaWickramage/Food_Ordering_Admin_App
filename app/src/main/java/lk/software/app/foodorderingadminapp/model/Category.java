package lk.software.app.foodorderingadminapp.model;

import java.util.ArrayList;
import java.util.List;

import lk.software.app.foodorderingadminapp.R;

public class Category {

    private String name;
    private String image;
private String documentId;
    public Category(String name, String image) {

        this.name = name;
        this.image = image;
    }

    public Category() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
