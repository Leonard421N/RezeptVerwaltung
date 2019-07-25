package wizardofba.rezeptverwaltung.Manage;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    private Context mainActivity;

    private Manager(Context context) {

        allRecipes = new ArrayList<>();
        allIngredients = new ArrayList<>();
        allRecipeImgs = new ArrayList<>();
        allIngredientImgs = new ArrayList<>();
        mainActivity = context;

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
                if(recipe.getImageUri() != null) {
                    allRecipeImgs.add(MediaLoader.getInstance().loadBitmapFromUri(Uri.parse(recipe.getImageUri())));
                } else allRecipeImgs.add(null);
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
        return mainActivity;
    }

    public void updateRecipe(final Recipe recipe) {

        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                if(recipe != null) {
                    int index = allRecipes.indexOf(recipe);
                    allRecipes.set(index, recipe);
                    if (recipe.getImageUri() != null) {
                        allRecipeImgs.set(index, MediaLoader.getInstance().loadBitmapFromUri(Uri.parse(recipe.getImageUri())));
                    } else {
                        allRecipeImgs.set(index, null);
                    }
                    recipeDatabase.daoAccess().updateRecipe(recipe);
                }
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
                if(ingredient.getImageUri() != null) {
                    allIngredientImgs.add(MediaLoader.getInstance().loadBitmapFromUri(Uri.parse(ingredient.getImageUri())));
                } else allIngredientImgs.add(null);
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
                if(ingredient != null) {
                    int index = allIngredients.indexOf(ingredient);
                    allIngredients.set(index, ingredient);
                    if (ingredient.getImageUri() != null) {
                        allIngredientImgs.set(index, MediaLoader.getInstance().loadBitmapFromUri(Uri.parse(ingredient.getImageUri())));
                    } else {
                        allIngredientImgs.set(index, null);
                    }
                    recipeDatabase.daoAccess().updateIngredient(ingredient);
                }
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

    public void generateRecipePrices() {
        for (Recipe tempRecipe: allRecipes) {
            tempRecipe.generatePrice();
        }
    }

    public void removeRecepi(final Recipe recipe) {
        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                int index = allRecipes.indexOf(recipe);
                allRecipes.remove(recipe);
                allRecipeImgs.remove(index);
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
                int index = allIngredients.indexOf(ingredient);
                allIngredients.remove(ingredient);
                allIngredientImgs.remove(index);
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

    public ArrayList<Bitmap> loadAllCustomIngredientBitmaps(final HashMap<Ingredient, Float> customIngredients) {

        final ArrayList<Bitmap> result = new ArrayList<>();

        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Ingredient tempIngredient: customIngredients.keySet()) {
                    try {
                        result.add(MediaLoader.getInstance()
                                .loadBitmapFromUri(Uri.parse(tempIngredient.getImageUri())));
                    } catch (Exception e) {
                        result.add(null);
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
        return result;
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

        public LinkedHashMap<Ingredient, Float> createCustomIngredientHashMap(HashMap<String, Float> customIngredients) {
            LinkedHashMap<Ingredient, Float> result = new LinkedHashMap<>();
            if(customIngredients != null) {
                List<String> ingredientIDs = new ArrayList<>(customIngredients.keySet());
                ArrayList<Float> amounts = new ArrayList<>(customIngredients.values());
                for (int i = 0; i < ingredientIDs.size(); i++) {
                    Ingredient tempIngredient = getIngredientPerUUID(ingredientIDs.get(i));
                    result.put(tempIngredient, amounts.get(i));
                }
            }
            return result;
        }

        public ArrayList<String> getAllIngredientNames() {
            ArrayList<String> result = new ArrayList<>();
            for (Ingredient ingredient: allIngredients) {
                result.add(ingredient.getName());
            }

            return result;
        }

        public static void hideKeyboard(Activity activity) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

}