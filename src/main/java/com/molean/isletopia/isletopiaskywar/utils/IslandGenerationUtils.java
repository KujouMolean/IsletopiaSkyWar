package com.molean.isletopia.isletopiaskywar.utils;

import com.google.gson.Gson;
import org.bukkit.*;
import org.bukkit.block.Biome;
import redis.clients.jedis.Jedis;

import java.util.*;

public class IslandGenerationUtils {
    public static Map<Biome, List<Location>> availableLocations = new HashMap<>();
    private static final int biggestIslandSize = 128;
    private static final int biomeSearchJumpBlocks = 6;
    private static final int biomeSearchSize = 10000;

    private static final Gson GSON = new Gson();

    public static String serialize(Map<Biome, List<Location>> raw) {
        Map<String, List<Map<String, Object>>> locations = new HashMap<>();

        for (Biome biome : raw.keySet()) {
            locations.put(biome.name(), new ArrayList<>());
            List<Map<String, Object>> maps = locations.get(biome.name());
            for (Location location : raw.get(biome)) {
                ;
                maps.add(location.serialize());
            }
        }
        return GSON.toJson(locations);
    }

    public static Map<Biome, List<Location>> deserialize(String string) {
        Map<Biome, List<Location>> raw = new HashMap<>();
        @SuppressWarnings("all")
        Map<String, List<Map<String, Object>>> map = new Gson().fromJson(string, Map.class);
        for (String s : map.keySet()) {
            List<Map<String, Object>> maps = map.get(s);
            raw.put(Biome.valueOf(s), new ArrayList<>());
            List<Location> locations = raw.get(Biome.valueOf(s));

            for (Map<String, Object> stringObjectMap : maps) {
                locations.add(Location.deserialize(stringObjectMap));
            }
        }
        return raw;
    }

    public static void init() {
        getSourceWorld();
        availableLocations = generateIslandLocations();
    }

    private static Map<Biome, List<Location>> generateIslandLocations() {
        try (Jedis jedis = JedisUtils.getJedis()) {
            if (jedis.exists("IslandLocations-" + getSourceWorld().getSeed())) {

                String islandLocations = jedis.get("IslandLocations-"+ getSourceWorld().getSeed());
                return deserialize(islandLocations);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        HashMap<Biome, List<Location>> locations = new HashMap<>();
        List<int[]> usedPositions = new ArrayList<>();
        for (int x = 0; x < biomeSearchSize - biggestIslandSize; x += biomeSearchJumpBlocks) {
            zLoop:
            for (int z = 0; z < biomeSearchSize - biggestIslandSize; z += biomeSearchJumpBlocks) {
                for (int[] pos : usedPositions) {
                    if (pos[0] <= x && x <= pos[0] + biggestIslandSize && pos[1] <= z && z <= pos[1] + biggestIslandSize) {
                        z += biggestIslandSize;
                        continue zLoop;
                    }
                }
                Biome biome = IslandGenerationUtils.getSourceWorld().getBiome(x, 180, z);
                if (isSuitableLocation(x, z, biome)) {
                    Location location = new Location(IslandGenerationUtils.getSourceWorld(), x, 0, z);
                    if (locations.containsKey(biome)) {
                        locations.get(biome).add(location);
                    } else {
                        List<Location> list = new ArrayList<>();
                        list.add(location);
                        locations.put(biome, list);
                    }
                    usedPositions.add(new int[]{x, z});
                    z += biggestIslandSize;
                }
            }
        }
        try (Jedis jedis = JedisUtils.getJedis()) {
            jedis.set("IslandLocations-" + getSourceWorld().getSeed(), serialize(locations));
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        return locations;
    }


    private static boolean isSuitableLocation(int xCorner, int zCorner, Biome biome) {

        for (int x = 0; x < IslandGenerationUtils.biggestIslandSize; x += biomeSearchJumpBlocks) {
            for (int z = 0; z < IslandGenerationUtils.biggestIslandSize; z += biomeSearchJumpBlocks) {
                if (IslandGenerationUtils.getSourceWorld().getBiome(xCorner + x, 180, zCorner + z) != biome) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Biome getRandomBiome() {
        ArrayList<Biome> biomes = new ArrayList<>(availableLocations.keySet());
        Biome biome = biomes.get(new Random().nextInt(biomes.size()));

        ArrayList<Biome> blackList = new ArrayList<>();
        blackList.add(Biome.RIVER);
        blackList.add(Biome.FROZEN_RIVER);

        if (blackList.contains(biome)) {
            return getRandomBiome();
        }

        if (biome.name().contains("OCEAN")) {
            return getRandomBiome();
        }

        return biome;
    }

    public static World getSourceWorld() {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getName().equals("islandsSource")) {
                return world;
            }
        }
        WorldCreator wc = new WorldCreator("islandsSource");
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);
        wc.generateStructures(false);
        World world = wc.createWorld();
        assert world != null;
        world.setDifficulty(Difficulty.PEACEFUL);
        return world;
    }

    public static boolean isBlockInIslandShape(int x, int y, int z, int islandSize) {
        return (Math.pow(x - islandSize / 2.0, 2) + (islandSize / Math.pow(y, 2) + 1.3) * Math.pow(y - islandSize / 2.0, 2) + Math.pow(z - islandSize / 2.0, 2))
                <= Math.pow(islandSize / 2.0, 2);
    }

}
