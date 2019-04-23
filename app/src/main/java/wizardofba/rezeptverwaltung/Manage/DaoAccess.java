package wizardofba.rezeptverwaltung.Manage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import wizardofba.rezeptverwaltung.Models.Ingredient;
import wizardofba.rezeptverwaltung.Models.Recepi;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleRecepi(Recepi recepi);
    @Insert
    void insertMultipleRecepis (List<Recepi> recepiList);
    @Query("SELECT * FROM Recepi WHERE recepiID = :recepiID")
    Recepi fetchOneMoviesbyRecepiID (int recepiID);
    @Update
    void updateRecepi (Recepi recepi);
    @Delete
    void deleteRecepi (Recepi recepi);

    @Insert
    void insertOnlySingleIngredient(Ingredient ingredient);
    @Insert
    void insertMultipleIngredients (List<Ingredient> ingredientList);
    @Query("SELECT * FROM Ingredient WHERE ingredientID = :ingredientID")
    Ingredient fetchOneMoviesbyIngredientID (int ingredientID);
    @Update
    void updateIngredient (Ingredient ingredient);
    @Delete
    void deleteIngredient (Ingredient ingredient);
}
