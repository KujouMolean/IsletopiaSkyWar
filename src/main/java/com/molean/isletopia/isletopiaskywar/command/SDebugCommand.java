package com.molean.isletopia.isletopiaskywar.command;

import com.molean.isletopia.isletopiaskywar.IslandManager;
import com.molean.isletopia.isletopiaskywar.objects.CuboidShape;
import com.molean.isletopia.isletopiaskywar.tasks.IslandGenerateTask;
import com.molean.isletopia.isletopiaskywar.utils.IslandGenerationUtils;
import com.molean.isletopia.isletopiaskywar.utils.ScheduleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SDebugCommand implements CommandExecutor {
    public SDebugCommand() {
        Objects.requireNonNull(Bukkit.getPluginCommand("sdebug")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        IslandManager.clear(16, () -> {
            ScheduleUtils.runAsync(() -> {
                final Biome outerBiome = IslandGenerationUtils.getRandomBiome();
                new IslandGenerateTask(outerBiome, 128, 5000, outerBlocks -> {
                    final Biome innerBiome = IslandGenerationUtils.getRandomBiome();
                    for (int i = 1; i <= 8; i++) {
                        final int finalI = i;
                        ScheduleUtils.runAsync(() -> {
                            long l = System.currentTimeMillis();
                            new IslandGenerateTask(innerBiome, 64, 500, innerBlocks -> {
                                IslandManager.getIslandRegion(finalI).applyCenter(new CuboidShape(innerBlocks, innerBiome), 500, () -> {
                                    int count = 0;
                                    List<Block> blocks = new ArrayList<>();
                                    HashSet<Block> chestBlocks = new HashSet<>();
                                    IslandManager.getIslandRegion(finalI).forEachHighestBlock(blocks::add);
                                    while (count++ < 10 && chestBlocks.size() < 4) {
                                        chestBlocks.add(blocks.get(new Random().nextInt(blocks.size())).getRelative(BlockFace.UP));
                                    }
                                    for (Block chestBlock : chestBlocks) {
                                        chestBlock.setType(Material.CHEST);
                                        Chest chest = (Chest) chestBlock.getState();
                                        chest.getBlockInventory().addItem(new ItemStack(Material.DIAMOND));
                                    }
                                });
                            }).run();
                            System.out.println(System.currentTimeMillis() - l);
                        });
                    }
                    IslandManager.getIslandRegion(0).applyCenter(new CuboidShape(outerBlocks, outerBiome), 5000, null);
                }).run();
            });
        });
        return true;
    }
}
