package wizardofba.rezeptverwaltung.Utility;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class RecipeHashMapConverter {

    @TypeConverter
    public static LinkedHashMap<String, Float> toHashMap(String list) {

        LinkedHashMap<String, Float> erg = new LinkedHashMap<String, Float>();
        ArrayList<String> hashmapAsString = new ArrayList<>();

        int j = 0;
        while(!list.equals("")) {
            hashmapAsString.add(list.split("[:]*:", 2)[0]);
            list = list.substring(hashmapAsString.get(j).length() + 1);
            hashmapAsString.add(list.split("[;]*;", 2)[0]);
            list = list.substring(hashmapAsString.get(j+1).length() + 1);
            j += 2;
        }

        int size = hashmapAsString.size();
        if(size > 0) {

            for (int i = 0; i < size; ) {

                erg.put(hashmapAsString.get(i), Float.valueOf(hashmapAsString.get(i + 1)));
                i += 2;
            }
        }
        return erg;
    }

    @TypeConverter
    public static String toString(HashMap<String, Float> list) {
        StringBuilder erg = new StringBuilder();

        ArrayList<String> strings = new ArrayList<>(list.keySet());
        ArrayList<Float> floats = new ArrayList<>(list.values());
        for(int k = 0; k < list.keySet().size();) {
            erg.append(strings.get(k)).append(":").append(floats.get(k)).append(";");
            k++;
        }
        return erg.toString();
    }
}
