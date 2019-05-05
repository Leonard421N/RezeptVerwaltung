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
    private List<Recepi> allRecepis;
    private List<Ingredient> allIngredients;

    private Manager(Context context) {

        allRecepis = new ArrayList<>();
        allIngredients = new ArrayList<>();

        recepiDatabase = Room.databaseBuilder(context,
                RecepiDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        loadDatabase();
    }

    public static synchronized Manager getInstance(Context context) {
        if(Manager.instance == null) {
            Manager.instance = new Manager(context);
        }
        return Manager.instance;
    }

    public void loadDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                allRecepis = recepiDatabase.daoAccess().fetchAllRecepis();
                allIngredients = recepiDatabase.daoAccess().fetchAllIngredients();
                MainActivity.notifyUpdate();
            }
        }).start();
    }

    public void addRecepi(final Recepi recepi) {

        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                allRecepis.add(recepi);
                recepiDatabase.daoAccess().insertOnlySingleRecepi(recepi);
            }
        });
        current.start();
        try {
            current.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MainActivity.notifyUpdate();
    }

    public void addIngredient(final Ingredient ingredient) {

        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                allIngredients.add(ingredient);
                recepiDatabase.daoAccess().insertOnlySingleIngredient(ingredient);
            }
        });
        current.start();
        try {
            current.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MainActivity.notifyUpdate();
    }

    public void removeRecepi(final Recepi recepi) {
        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                allRecepis.remove(recepi);
                recepiDatabase.daoAccess().deleteRecepi(recepi);
            }
        });
        current.start();
        try {
            current.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MainActivity.notifyUpdate();
    }

    public List<Recepi> getAllRecepis() {
        return allRecepis;
    }

    public List<Ingredient> getAllIngredients() {
        return allIngredients;
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
