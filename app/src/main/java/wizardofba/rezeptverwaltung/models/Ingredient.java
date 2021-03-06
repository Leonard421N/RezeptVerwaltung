package wizardofba.rezeptverwaltung.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.UUID;

import wizardofba.rezeptverwaltung.utility.IngredientPriceConverter;

@Entity
public class Ingredient {

    @NonNull
    @PrimaryKey
    private String ingredientID = UUID.randomUUID().toString();

    //<Amount, Price>
    @TypeConverters(IngredientPriceConverter.class)
    private Pair<Float, Float> priceAndAmount;

    private String imageUri;

    private String name;
    private String description;
    private String unit;
    private String amazonUrl;

    public Ingredient() {
    }

    @Ignore
    public Ingredient(String name) {
        this.name = name;
        this.unit = "ml";
    }

    @Ignore
    public Ingredient(String name, float amount, float price) {
        this.name = name;
        this.priceAndAmount = new Pair<>(amount, price);
        this.unit = "ml";
    }


    /** Returns Price per 1 unit of ingredient
     * like 1g = 0.12 cents */
    public Float getBasePrice() {
        return this.priceAndAmount.second/this.priceAndAmount.first;
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

    public Pair<Float, Float> getPriceAndAmount() {
        return priceAndAmount;
    }

    public void setPriceAndAmount(Pair<Float, Float> priceAndAmount) {
        this.priceAndAmount = priceAndAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setIngredientID(@NonNull String ingredientID) {
        this.ingredientID = ingredientID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getAmazonUrl() {
        return amazonUrl;
    }

    public void setAmazonUrl(String amazonUrl) {
        this.amazonUrl = amazonUrl;
    }

    public String getAmazonID() {
        try {
            return amazonUrl.split("/dp/|/gp/products/|/d/", 2)[1].split("/",2)[0];
        } catch (Exception e) {
            return null;
        }
    }
}
