import com.google.gson.Gson;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeLocationTest {
    public static void main(String[] args) {

        HashMap<String, List<Map<String,Object>>> locations = new HashMap<>();

        ArrayList<Map<String,Object>> objects = new ArrayList<>();
        objects.add(new Location(null, 1, 2, 3).serialize());
        objects.add(new Location(null, 2, 3, 4).serialize());
        locations.put("Biome.BADLANDS", objects);
        locations.put("Biome.BAMBOO_JUNGLE_HILLS", objects);
        String s = new Gson().toJson(locations);
        System.out.println();

        Map map = new Gson().fromJson(s, Map.class);
        System.out.println(map.get("Biome.BADLANDS").getClass().getSimpleName());

    }
}
