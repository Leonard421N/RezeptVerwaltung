package wizardofba.rezeptverwaltung.Manage;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import wizardofba.rezeptverwaltung.MainActivity;
import wizardofba.rezeptverwaltung.Models.Ingredient;
import wizardofba.rezeptverwaltung.Models.Recipe;
import wizardofba.rezeptverwaltung.Utility.MediaLoader;

public class Manager {

    private static final String DATABASE_NAME = "recipes_db";
    private static Manager instance;

    private RecipeDatabase recipeDatabase;
    private List<Recipe> allRecipes;
    private List<Ingredient> allIngredients;
    private List<Bitmap> allRecipeImgs;
    private List<Bitmap> allIngredientImgs;
    private Context mainactivity;

    private Manager(Context context) {

        allRecipes = new ArrayList<>();
        allIngredients = new ArrayList<>();
        allRecipeImgs = new ArrayList<>();
        allIngredientImgs = new ArrayList<>();
        mainactivity = context;

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

    public synchronized void loadDatabase() {
        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                allRecipes = recipeDatabase.daoAccess().fetchAllRecipes();
                allIngredients = recipeDatabase.daoAccess().fetchAllIngredients();
                loadAllIngredientBitmaps();
                loadAllRecipeBitmaps();
            }
        });
        current.start();
        try {
            current.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public Context getMainactivityContext() {
        return mainactivity;
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

    public void updateIngredient(final Ingredient ingredient) {

        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < allRecipes.size(); i++) {
                    if(ingredient.getIngredientID().equals(allRecipes.get(i))) {
                        allIngredients.set(i, ingredient);
                    }
                }
                recipeDatabase.daoAccess().updateIngredient(ingredient);
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

    public void removeIngredient(final Ingredient ingredient) {
        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                allIngredients.remove(ingredient);
                recipeDatabase.daoAccess().deleteIngredient(ingredient);
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

    private void loadAllRecipeBitmaps() {

        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Recipe tempRecipe: allRecipes) {
                    try {
                        allRecipeImgs.add(MediaLoader.getInstance()
                                .loadBitmapFromUri(Uri.parse(tempRecipe.getImageUri())));
                    } catch (Exception e) {
                        allRecipeImgs.add(null);
                    }
                }
            }
        });
        current.start();
        try {
            current.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadAllIngredientBitmaps() {

        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Ingredient tempIngredient: allIngredients) {
                    try {
                        allIngredientImgs.add(MediaLoader.getInstance()
                                .loadBitmapFromUri(Uri.parse(tempIngredient.getImageUri())));
                    } catch (Exception e) {
                        allIngredientImgs.add(null);
                    }
                }
            }
        });
        current.start();
        try {
            current.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

        public List<Bitmap> getAllRecipeImgs() {
            return allRecipeImgs;
        }

        public List<Bitmap> getAllIngredientImgs() {
            return allIngredientImgs;
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