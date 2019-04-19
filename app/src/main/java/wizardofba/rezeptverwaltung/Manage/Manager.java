package wizardofba.rezeptverwaltung.Manage;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.UUID;

import wizardofba.rezeptverwaltung.Models.Ingredient;
import wizardofba.rezeptverwaltung.Models.Recepi;

public class Manager {

    private static Manager instance;

    private ArrayList<Recepi> allRecepis; //load somewhere
    private ArrayList<Ingredient> allIngredients; //same here

    private Manager() {

        allRecepis = new ArrayList<>();
        allIngredients = new ArrayList<>();

        allRecepis.add(new Recepi("Kaffee", 1.0f));
        allRecepis.add(new Recepi("Pommes", 2.0f));
        allRecepis.add(new Recepi("Schwarzwälder Kirschtorte", 42.10f));
        allRecepis.add(new Recepi("Currywurst mit Pommes", 4.0f));
        allRecepis.add(new Recepi("Räuberteller", 0.0f));
    }

    public static synchronized Manager getInstance() {
        if(Manager.instance == null) {
            Manager.instance = new Manager();
        }
        return Manager.instance;
    }

    public ArrayList<Recepi> getAllRecepis() {
        return allRecepis;
    }

    @Nullable
    public Recepi getRecepiPerUUID(UUID id) {
        for (Recepi recepi: allRecepis) {
            if(id.equals(recepi.getId())) {
                return recepi;
            }
        }
        return null;
    }

    @Nullable
    public Ingredient getIngredientPerUUID(UUID id) {
        for (Ingredient ingredient: allIngredients) {
            if(id.equals(ingredient.getId())) {
                return ingredient;
            }
        }
        return null;
    }
}
