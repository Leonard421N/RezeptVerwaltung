package wizardofba.rezeptverwaltung.Manage;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.UUID;

import wizardofba.rezeptverwaltung.Models.Ingredient;
import wizardofba.rezeptverwaltung.Models.Recepi;

public class Manager {

    private static Manager instance;

    private ArrayList<Recepi> recepis; //load somewhere
    private ArrayList<Ingredient> ingredients; //same here

    private Manager() {}

    public static synchronized Manager getInstance() {
        if(Manager.instance == null) {
            Manager.instance = new Manager();
        }
        return Manager.instance;
    }

    public ArrayList<Recepi> getRecepis() {
        return recepis;
    }

    @Nullable
    public Recepi getRecepiPerUUID(UUID id) {
        for (Recepi recepi: recepis) {
            if(id.equals(recepi.getId())) {
                return recepi;
            }
        }
        return null;
    }

    @Nullable
    public Ingredient getIngredientPerUUID(UUID id) {
        for (Ingredient ingredient: ingredients) {
            if(id.equals(ingredient.getId())) {
                return ingredient;
            }
        }
        return null;
    }
}
