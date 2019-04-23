package wizardofba.rezeptverwaltung.Models;

import android.arch.persistence.room.TypeConverter;
import android.util.Pair;

import java.util.Arrays;
import java.util.List;

public class IngredientPriceConverter {

    @TypeConverter
    public static Pair<Float, Float> toPrice(String pricePair) {
        List<String> pricePairList = Arrays.asList(pricePair.split("\\s*:\\s*"));
        float a = Float.parseFloat(pricePairList.get(0));
        float b = Float.parseFloat(pricePairList.get(1));
        return new Pair<Float, Float>(a, b);
    }

    @TypeConverter
    public static String toString(Pair<Float, Float> pricePair) {
        return pricePair.first + ":" + pricePair.second;
    }
}
