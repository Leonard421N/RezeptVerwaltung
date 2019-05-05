package wizardofba.rezeptverwaltung.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import wizardofba.rezeptverwaltung.MainActivity;
import wizardofba.rezeptverwaltung.Utility.RecipeHashMapConverter;

@Entity
public class Recipe {

    @NonNull
    @PrimaryKey
    private String recipeID = UUID.randomUUID().toString();

    @TypeConverters(RecipeHashMapConverter.class)
    private HashMap<String, Float> ingredients;
    @TypeConverters(RecipeHashMapConverter.class)
    private HashMap<String, Float> recipes;

    private String name;
    private String picUri;
    private Float price;
    private String description;

    public Recipe() {
        initClass();
    }

    @Ignore
    public Recipe(String name) {
        initClass();

        this.name = name;
    }

    @Ignore
    public Recipe(String name, Float price) {
        initClass();

        this.name = name;
        this.price = price;
    }

    private void initClass() {
        this.ingredients = new HashMap<String, Float>();
        this.recipes = new HashMap<String, Float>();
        this.price = 0f;
    }

    public void addIngredient(String ingr, Float amount) {
        this.ingredients.put(ingr, amount);
    }

    public void addRecipe(String id) {
        this.recipes.put(id, 1.0f);
    }

    public void addRecipe(String id, Float amount) {
        this.recipes.put(id, amount);
    }

    public void removeIngredient(String id) {
        if(!ingredients.isEmpty()) {
            for (String i: ingredients.keySet()) {
                if(i.equals(id)) {
                    ingredients.remove(i);
                }
            }
        }
    }

    public void generatePrice() {
        float tempPrice = 0f;
        Recipe tempRecipe;
        Ingredient tempIngredient;
        int index = 0;


        ArrayList<Float> tempRecepis = new ArrayList<>(recipes.values());
        Float tempAmount;
        for(String id: recipes.keySet()) {
            tempAmount = tempRecepis.get(index);
            tempRecipe = MainActivity.getManager().getRecepiPerUUID(id);
            if(tempRecipe != null) {
                tempPrice += tempRecipe.getPrice()*tempAmount;
            }
            index++;
        }

        index = 0;
        ArrayList<Float> tempIngredients = new ArrayList<>(ingredients.values());
        for (String id: ingredients.keySet()) {
            tempAmount = tempIngredients.get(index);
            tempIngredient = MainActivity.getManager().getIngredientPerUUID(id);
            if(tempIngredient != null) {
                tempPrice += tempIngredient.getBasePrice()*tempAmount;
            }
            index++;
        }

        this.price = tempPrice;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Float> getRecipes() {
        return this.recipes;
    }

    public Float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRecipeID(@NonNull String recipeID) {
        this.recipeID = recipeID;
    }

    public HashMap<String, Float> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<String, Float> ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipes(HashMap<String, Float> recipes) {
        this.recipes = recipes;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
