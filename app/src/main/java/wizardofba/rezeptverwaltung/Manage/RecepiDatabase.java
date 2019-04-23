package wizardofba.rezeptverwaltung.Manage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import wizardofba.rezeptverwaltung.Models.Ingredient;
import wizardofba.rezeptverwaltung.Models.Recepi;

@Database(entities = {Recepi.class, Ingredient.class}, version = 1, exportSchema = false)
public abstract class RecepiDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess() ;
}
