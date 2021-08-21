package com.molean.isletopia.isletopiaskywar;

import com.molean.isletopia.isletopiaskywar.objects.CuboidRegion;
import com.molean.isletopia.isletopiaskywar.tasks.MassiveChunkTask;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;

public class IslandManager {

    public static Location getCenterLocation() {
        return new Location(Bukkit.getWorld("world"), 255, 0, 255);
    }


    public static void clear(int chunkPerTicks,Runnable runnable){
        Listener listener = new Listener() {
            @EventHandler
            public void on(BlockFromToEvent event) {
                event.setCancelled(true);
            }
        };
        Bukkit.getPluginManager().registerEvents(listener, JavaPlugin.getPlugin(IsletopiaSkyWar.class));
        World world = Bukkit.getWorld("world");
        assert world != null;
        HashSet<Chunk> chunks = new HashSet<>();
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                chunks.add(world.getChunkAt(i, j));
            }
        }
        new MassiveChunkTask(chunks, chunk -> {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    for (int k = 0; k < 255; k++) {
                        chunk.getBlock(i, k, j).setType(Material.AIR);
                        chunk.getBlock(i, k, j).setBiome(Biome.THE_VOID);

                    }
                }
            }
            for (Entity entity :chunk.getEntities()) {
                try {
                    entity.remove();
                } catch (Exception ignore) {
                }
            }
        }, chunkPerTicks, ()->{
            HandlerList.unregisterAll(listener);
            runnable.run();
        }).run();
    }

    public static CuboidRegion getIslandRegion(int x) {
        World world = Bukkit.getWorld("world");
        Location bot = null, top = null;
        assert world != null;
        switch (x) {
            case 0 -> {
                bot = getCenterLocation().add(-4 * 16, 0, -4 * 16);
                top = getCenterLocation().add(4 * 16, 255, 4 * 16);
            }
            case 1 -> {
                bot = getCenterLocation().add(-0.5 * 4 * 16, 0, 2.5 * 4 * 16);
                top = getCenterLocation().add(0.5 * 4 * 16, 255, 3.5 * 4 * 16);
            }
            case 2 -> {
                bot = getCenterLocation().add(2 * 4 * 16, 0, 2 * 4 * 16);
                top = getCenterLocation().add(3 * 4 * 16, 255, 3 * 4 * 16);
            }
            case 3 -> {
                bot = getCenterLocation().add(2.5 * 4 * 16, 0, -0.5 * 4 * 16);
                top = getCenterLocation().add(3.5 * 4 * 16, 255, 0.5 * 4 * 16);
            }
            case 4 -> {
                bot = getCenterLocation().add(2 * 4 * 16, 0, -2 * 4 * 16);
                top = getCenterLocation().add(3 * 4 * 16, 255, -3 * 4 * 16);
            }
            case 5 -> {
                bot = getCenterLocation().add(-0.5 * 4 * 16, 0, -2.5 * 4 * 16);
                top = getCenterLocation().add(0.5 * 4 * 16, 255, -3.5 * 4 * 16);
            }
            case 6 -> {
                bot = getCenterLocation().add(-2 * 4 * 16, 0, -2 * 4 * 16);
                top = getCenterLocation().add(-3 * 4 * 16, 255, -3 * 4 * 16);
            }
            case 7 -> {
                bot = getCenterLocation().add(-2.5 * 4 * 16, 0, -0.5 * 4 * 16);
                top = getCenterLocation().add(-3.5 * 4 * 16, 255, 0.5 * 4 * 16);
            }
            case 8 -> {
                bot = getCenterLocation().add(-2 * 4 * 16, 0, 2 * 4 * 16);
                top = getCenterLocation().add(-3 * 4 * 16, 255, 3 * 4 * 16);
            }
            default -> throw new IllegalStateException("Unexpected value: " + x);
        }
        return new CuboidRegion(bot, top);
    }
}
