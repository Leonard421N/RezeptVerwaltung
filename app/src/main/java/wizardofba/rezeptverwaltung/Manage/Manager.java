package wizardofba.rezeptverwaltung.Manage;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import wizardofba.rezeptverwaltung.MainActivity;
import wizardofba.rezeptverwaltung.Models.Ingredient;
import wizardofba.rezeptverwaltung.Models.Recipe;

public class Manager {

    private static final String DATABASE_NAME = "recipes_db";
    private static Manager instance;

    private RecipeDatabase recipeDatabase;
    private List<Recipe> allRecipes;
    private List<Ingredient> allIngredients;

    private Manager(Context context) {

        allRecipes = new ArrayList<>();
        allIngredients = new ArrayList<>();

        recipeDatabase = Room.databaseBuilder(context,
                RecipeDatabase.class, DATABASE_NAME)
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
                allRecipes = recipeDatabase.daoAccess().fetchAllRecipes();
                allIngredients = recipeDatabase.daoAccess().fetchAllIngredients();
                MainActivity.notifyUpdate();
            }
        }).start();
    }

    public void addRecepi(final Recipe recipe) {

        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                allRecipes.add(recipe);
                recipeDatabase.daoAccess().insertOnlySingleRecipe(recipe);
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

    public void updateRecipe(final Recipe recipe) {

        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < allRecipes.size(); i++) {
                    if(recipe.getRecipeID().equals(allRecipes.get(i))) {
                        allRecipes.set(i, recipe);
                    }
                }
                recipeDatabase.daoAccess().updateRecipe(recipe);
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
                recipeDatabase.daoAccess().insertOnlySingleIngredient(ingredient);
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

    public void removeRecepi(final Recipe recipe) {
        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                allRecipes.remove(recipe);
                recipeDatabase.daoAccess().deleteRecipe(recipe);
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

    public List<Recipe> getAllRecipes() {
        return allRecipes;
    }

    public List<Ingredient> getAllIngredients() {
        return allIngredients;
    }

    @Nullable
    public Recipe getRecepiPerUUID(String id) {
        for (Recipe recipe : allRecipes) {
            if(id.equals(recipe.getRecipeID())) {
                return recipe;
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
