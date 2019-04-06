package wizardofba.rezeptverwaltung.Models;

import android.net.Uri;

import java.util.UUID;

public class Ingredient {

    private UUID id;

    private String name;
    private float price;
    private Uri picUri;
    private String description;

    public Ingredient() {
        initClass();
    }
    public Ingredient(String name) {
        initClass();

        this.name = name;
    }

    public Ingredient(String name, float price) {
        initClass();

        this.name = name;
        this.price = price;
    }

    private void initClass() {
        if(id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
