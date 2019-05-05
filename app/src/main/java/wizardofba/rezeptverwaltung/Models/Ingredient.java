package wizardofba.rezeptverwaltung.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.UUID;

import wizardofba.rezeptverwaltung.Utility.IngredientPriceConverter;

@Entity
public class Ingredient {

    @NonNull
    @PrimaryKey
    private String ingredientID = UUID.randomUUID().toString();

    @TypeConverters(IngredientPriceConverter.class)
    private Pair<Float, Float> price;

    private String name;
    private String picUri;
    private String description;

    public Ingredient() {
    }

    @Ignore
    public Ingredient(String name) {
        this.name = name;
    }

    @Ignore
    public Ingredient(String name, float amount, float price) {
        this.name = name;
        this.price = new Pair<>(amount, price);
    }


    /** Returns Price per 1 unit of ingredient
     * like 1g = 0.12 cents */
    public Float getBasePrice() {
        return this.price.second/this.price.first;
    }

    @NonNull
    public String getIngredientID() {
        return ingredientID;
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

    public void setIngredientID(@NonNull String ingredientID) {
        this.ingredientID = ingredientID;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
