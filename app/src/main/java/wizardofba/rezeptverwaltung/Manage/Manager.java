package wizardofba.rezeptverwaltung.Manage;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import wizardofba.rezeptverwaltung.MainActivity;
import wizardofba.rezeptverwaltung.Models.Ingredient;
import wizardofba.rezeptverwaltung.Models.Recepi;

public class Manager {

    private static final String DATABASE_NAME = "recepis_db";
    private static Manager instance;

    private RecepiDatabase recepiDatabase;
    private List<Recepi> allRecepis; //load somewhere
    private ArrayList<Ingredient> allIngredients; //same here
    private Context mainContext;

    private Manager(Context context) {

        this.mainContext = context;
        allRecepis = new ArrayList<>();
        allIngredients = new ArrayList<>();

        recepiDatabase = Room.databaseBuilder(context,
                RecepiDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        loadDatabase();
        /*
        allRecepis.add(new Recepi("Kaffee", 1.0f));
        allRecepis.add(new Recepi("Pommes", 2.0f));
        allRecepis.add(new Recepi("Schwarzwälder Kirschtorte", 42.10f));
        allRecepis.add(new Recepi("Currywurst mit Pommes", 4.0f));
        allRecepis.add(new Recepi("Räuberteller", 0.0f));
        */
    }

    public static synchronized Manager getInstance(Context context) {
        if(Manager.instance == null) {
            Manager.instance = new Manager(context);
        }
        return Manager.instance;
    }

    private void loadDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                allRecepis = recepiDatabase.daoAccess().fetchAllRecepis();
                MainActivity.notifyUpdate();
            }
        }).start();
    }

    public void addRecepi(final Recepi recepi) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                allRecepis.add(recepi);
                recepiDatabase.daoAccess().insertOnlySingleRecepi(recepi);
            }
        }) .start();
    }

    List<Recepi> getAllRecepis() {
        return allRecepis;
    }
    
    private void removeRecepi(String id) {
        for (Recepi recepi: allRecepis) {
            if(id.equals(recepi.getRecepiID())) {
                allRecepis.remove(recepi);
            }
        }
    }

    @Nullable
    public Recepi getRecepiPerUUID(String id) {
        for (Recepi recepi: allRecepis) {
            if(id.equals(recepi.getRecepiID())) {
                return recepi;
            }
        }
        return null;
    }

    @Nullable
    public Ingredient getIngredientPerUUID(String id) {
        for (Ingredient ingredient: allIngredients) {
            if(id.equals(ingredient.getIngredientID())) {
                return ingredient;
            }
        }
        return null;
    }
}
