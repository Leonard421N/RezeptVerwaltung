package wizardofba.rezeptverwaltung.Models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import wizardofba.rezeptverwaltung.Manage.Manager;

public class Recepi {

    private UUID id;

    private HashMap<UUID, Float> ingredients;
    private HashMap<UUID, Float> recipes;
    private String name;
    private Uri picUri;
    private Float price;
    private String description;

    public Recepi() {
        initClass();
    }

    public Recepi(String name) {
        initClass();

        this.name = name;
    }

    private void initClass() {
        if(id == null) {
            this.id = UUID.randomUUID();
        }
        this.ingredients = new HashMap<UUID, Float>();
        this.recipes = new HashMap<UUID, Float>();
        this.price = 0f;
    }

    public void addIngredient(UUID ingr, Float amount) {
        this.ingredients.put(ingr, amount);
    }

    public void addRecipe(UUID id) {
        this.recipes.put(id, 1.0f);
    }

    public void addRecipe(UUID id, Float amount) {
        this.recipes.put(id, amount);
    }

    public void removeIngredient(UUID id) {
        if(!ingredients.isEmpty()) {
            for (UUID i: ingredients.keySet()) {
                if(i.equals(id)) {
                    ingredients.remove(i);
                }
            }
        }
    }

    public void generatePrice() {
        float tempPrice = 0f;
        Recepi tempRecepi;
        Ingredient tempIngredient;
        int index = 0;


        ArrayList<Float> tempRecepis = new ArrayList<>(recipes.values());
        Float tempAmount;
        for(UUID id: recipes.keySet()) {
            tempAmount = tempRecepis.get(index);
            tempRecepi = Manager.getInstance().getRecepiPerUUID(id);
            if(tempRecepi != null) {
                tempPrice += tempRecepi.getPrice()*tempAmount;
            }
            index++;
        }

        index = 0;
        ArrayList<Float> tempIngredients = new ArrayList<>(ingredients.values());
        for (UUID id: ingredients.keySet()) {
            tempAmount = tempIngredients.get(index);
            tempIngredient = Manager.getInstance().getIngredientPerUUID(id);
            if(tempIngredient != null) {
                tempPrice += tempIngredient.getBasePrice()*tempAmount;
            }
            index++;
        }

        this.price = tempPrice;
    }

    public Uri getPicUri() {
        return picUri;
    }

    public void setPicUri(Uri picUri) {
        this.picUri = picUri;
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

    public HashMap<UUID, Float> getRecipes() {
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
}
