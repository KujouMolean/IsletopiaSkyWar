package com.molean.isletopia.isletopiaskywar.tasks;

import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.function.Consumer;

public class MassiveChunkTask extends BukkitRunnable {
    private final Chunk[] chunks;
    private final Consumer<Chunk> chunkConsumer;
    private final int chunkPerTick;
    private int index = 0;
    private final Runnable runnable;


    public MassiveChunkTask(Set<Chunk> chunkSet, Consumer<Chunk> consumer) {
        this(chunkSet, consumer, 60, () -> {
        });
    }

    public MassiveChunkTask(Set<Chunk> chunkSet, Consumer<Chunk> consumer, int chunkPerTick, Runnable runnable) {
        this.runnable = runnable;
        this.chunkConsumer = consumer;
        this.chunkPerTick = chunkPerTick;


        this.chunks = chunkSet.toArray(new Chunk[0]);
    }

    public void run() {
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(IsletopiaSkyWar.class), (task) -> {
            int count = 0;
            boolean end = true;
            for (; index < chunks.length; index++) {
                chunkConsumer.accept(chunks[index]);
                count++;
                end = false;
                if (count >= chunkPerTick) {
                    return;
                }
            }
            if (end) {
                task.cancel();
                if (runnable != null) {

                    runnable.run();
                }

            }

        }, 1, 1);
    }
}
