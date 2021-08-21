package com.molean.isletopia.isletopiaskywar.tasks;

import com.molean.isletopia.isletopiaskywar.utils.IslandGenerationUtils;
import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import com.molean.isletopia.isletopiaskywar.objects.RichBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Consumer;

public class IslandGenerateTask {
    private final HashSet<RichBlockData> richBlockData = new HashSet<>();
    private int x;
    private int y;
    private int z;
    private final int size;
    private final Biome biome;
    private final World sourceWorld = IslandGenerationUtils.getSourceWorld();
    private final Location copyLocation;
    private final int blocksPerTick;
    private final Consumer<HashSet<RichBlockData>> consumer;


    public IslandGenerateTask(Biome biome, int size, int blocksPerTick, Consumer<HashSet<RichBlockData>> consumer) {
        Map<Biome, List<Location>> availableLocations = IslandGenerationUtils.availableLocations;
        if (availableLocations.getOrDefault(biome, new ArrayList<>()).size() == 0)
            throw new IllegalArgumentException();

        Location sourceLocation = availableLocations.get(biome).
                get(new Random().nextInt(availableLocations.get(biome).size()));

        int centerY = 100;
        int centerX = sourceLocation.getBlockX() + size / 2;
        int centerZ = sourceLocation.getBlockZ() + size / 2;
        while (centerY > 0) {
            Material material = sourceWorld.getBlockAt(centerX, centerY, centerZ).getBlockData().getMaterial();
            if (material.isSolid() && !material.isBurnable() && !material.name().contains("MUSHROOM")) {
                break;
            }
            centerY--;
        }
        if (centerY < 0) {
            throw new IllegalArgumentException();
        }

        sourceLocation.setY(centerY);
        copyLocation = new Location(sourceWorld,
                sourceLocation.getBlockX() - size / 2.0,
                sourceLocation.getBlockY() - size / 2.0,
                sourceLocation.getBlockZ() - size / 2.0);
        x = 0;
        z = 0;
        y = size;
        this.size = size;
        this.biome = biome;
        this.blocksPerTick = blocksPerTick;
        this.consumer = consumer;
    }

    public void run() {
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(IsletopiaSkyWar.class), (task) -> {
            int count = 0;
            boolean hasBlock = false;
            for (; x < size; x++) {
                for (; z < size; z++) {
                    double stalactiteHeight = 8;
                    int maxYAdd = (int) (size / 2d + 4 * 0.7 + stalactiteHeight);
                    for (; y >= -maxYAdd; y--) {
                        hasBlock = true;
                        Block sb = sourceWorld.getBlockAt(copyLocation.clone().add(x, y, z));
                        BlockData data = sb.getBlockData();
                        if (IslandGenerationUtils.isBlockInIslandShape(x, y, z, size)) {
                            richBlockData.add(new RichBlockData(x, y, z, data, biome));
                        }
                        if (++count >= blocksPerTick) {
                            return;
                        }
                    }
                    y = size;
                }
                z = 0;
            }
            if (!hasBlock) {
                task.cancel();
                consumer.accept(richBlockData);
            }
        }, 1, 1);
    }
}
