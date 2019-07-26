package wizardofba.rezeptverwaltung.manage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import wizardofba.rezeptverwaltung.models.Ingredient;
import wizardofba.rezeptverwaltung.models.Recipe;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleRecipe(Recipe recipe);
    @Insert
    void insertMultipleRecipes(List<Recipe> recipeList);
    @Query("SELECT * FROM Recipe WHERE recipeID = :recipeID")
    Recipe fetchOneRecepisbyRecipeID(int recipeID);
    @Query("SELECT * FROM Recipe")
    List<Recipe> fetchAllRecipes();
    @Update
    void updateRecipe(Recipe recipe);
    @Delete
    void deleteRecipe(Recipe recipe);

    @Insert
    void insertOnlySingleIngredient(Ingredient ingredient);
    @Insert
    void insertMultipleIngredients (List<Ingredient> ingredientList);
    @Query("SELECT * FROM Ingredient WHERE ingredientID = :ingredientID")
    Ingredient fetchOneIngredientsbyIngredientID (int ingredientID);
    @Query("SELECT * FROM Ingredient")
    List<Ingredient> fetchAllIngredients();
    @Update
    void updateIngredient (Ingredient ingredient);
    @Delete
    void deleteIngredient (Ingredient ingredient);
}
