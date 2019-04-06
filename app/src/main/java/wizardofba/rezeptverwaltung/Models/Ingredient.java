package wizardofba.rezeptverwaltung.Models;

import android.net.Uri;
import android.util.Pair;

import java.util.UUID;

public class Ingredient {

    private UUID id;

    private String name;
    private Pair<Float, Float> price;
    private Uri picUri;
    private String description;

    public Ingredient() {
        initClass();
    }
    public Ingredient(String name) {
        initClass();

        this.name = name;
    }

    public Ingredient(String name, float amount, float price) {
        initClass();

        this.name = name;
        this.price = new Pair<>(amount, price);
    }

    private void initClass() {
        if(id == null) {
            this.id = UUID.randomUUID();
        }
    }


    /** Returns Price per 1 unit of ingredient
     * like 1g = 0.12 cents */
    public Float getBasePrice() {
        return this.price.second/this.price.first;
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

    public Pair<Float, Float> getPrice() {
        return price;
    }

    public void setPrice(Pair<Float, Float> price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
