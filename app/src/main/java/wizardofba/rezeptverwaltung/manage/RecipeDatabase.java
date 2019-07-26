package wizardofba.rezeptverwaltung.manage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import wizardofba.rezeptverwaltung.models.Ingredient;
import wizardofba.rezeptverwaltung.models.Recipe;

@Database(entities = {Recipe.class, Ingredient.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess() ;
}
