package wizardofba.rezeptverwaltung.Models;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RecepiHashMapConverter {

    @TypeConverter
    public static HashMap<String, Float> toHashMap(String list) {

        HashMap<String, Float> erg = new HashMap<String, Float>();

        List<String> hashmapAsString = Arrays.asList(list.split("\\s*:\\s*;"));

        for(int i = 0; i < hashmapAsString.size();) {

            erg.put(hashmapAsString.get(i), Float.valueOf(hashmapAsString.get(i+1)));
            i += 2;
        }
        return erg;
    }

    @TypeConverter
    public static String toString(HashMap<String, Float> list) {
        StringBuilder erg = new StringBuilder();

        ArrayList<String> strings = new ArrayList<>(list.keySet());
        ArrayList<Float> floats = new ArrayList<>(list.values());
        for(int k = 0; k < list.keySet().size();) {
            erg.append(strings.get(k)).append(":").append(floats.get(k + 1)).append(";");
            k += 2;
        }
        return erg.toString();
    }
}
