package com.molean.isletopia.isletopiaskywar.objects;

import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class CuboidShape {
    private final BlockData[][][] data;
    private final Biome biome;

    private final int a;
    private final int b;
    private final int c;

    public CuboidShape(Set<RichBlockData> blockSet, Biome biome) {
        blockSet.removeIf(richBlockData -> richBlockData.blockData.getMaterial().isAir());

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (RichBlockData richBlockData : blockSet) {
            minX = Math.min(richBlockData.x, minX);
            minY = Math.min(richBlockData.y, minY);
            minZ = Math.min(richBlockData.z, minZ);

            maxX = Math.max(richBlockData.x, maxX);
            maxY = Math.max(richBlockData.y, maxY);
            maxZ = Math.max(richBlockData.z, maxZ);
        }

        a = maxX - minX + 1;
        b = maxY - minY + 1;
        c = maxZ - minZ + 1;

        data = new BlockData[a][b][c];
        this.biome = biome;

        BlockData airBlockData = Bukkit.createBlockData(Material.AIR);
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                for (int k = 0; k < c; k++) {
                    data[i][j][k] = airBlockData.clone();
                }
            }
        }

        for (RichBlockData richBlockData : blockSet) {
            int x = richBlockData.x - minX;
            int y = richBlockData.y - minY;
            int z = richBlockData.z - minZ;
            data[x][y][z] = richBlockData.blockData;
        }
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    public BlockData getBlock(int x, int y, int z) {
        return data[x][y][z];
    }

    private int i, j, k;

    public void put(Location location, int blockPerTick, Runnable runnable) {

        i = 0;
        j = 0;
        k = 0;

        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(IsletopiaSkyWar.class), (task) -> {
            boolean end = true;
            int count = 0;
            for (; i < a; i++) {
                for (; j < b; j++) {
                    for (; k < c; k++) {
                        location.clone().add(i, j, k).getBlock().setBlockData(data[i][j][k]);
                        end = false;
                        if (++count >= blockPerTick) {
                            return;
                        }
                    }
                    k = 0;
                }
                j = 0;
            }
            if (end) {
                task.cancel();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }, 1, 1);
    }

    public Biome getBiome() {
        return biome;
    }
}
