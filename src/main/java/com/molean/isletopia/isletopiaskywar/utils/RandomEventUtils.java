package com.molean.isletopia.isletopiaskywar.utils;

import com.molean.isletopia.isletopiaskywar.IslandManager;
import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import com.molean.isletopia.isletopiaskywar.tasks.MassiveChunkTask;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Random;

public class RandomEventUtils {
    public static void tntRain() {
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(IsletopiaSkyWar.class), () -> {
            for (int i = 0; i < 15; i++) {
                ThreadUtils.sleepSecond();
                MessageUtils.broadcastChat((15 - i) + "秒后会降落tnt流星雨");
            }
            World world = Bukkit.getWorld("world");
            assert world != null;
            HashSet<Chunk> chunks = new HashSet<>();
            for (int i = 0; i < 32; i++) {
                for (int j = 0; j < 32; j++) {
                    chunks.add(world.getChunkAt(i, j));
                }
            }
            Random random = new Random();
            new MassiveChunkTask(chunks, chunk -> {
                Location location = chunk.getBlock(8, 255, 8).getLocation();
                TNTPrimed entity = (TNTPrimed) world.spawnEntity(location, EntityType.PRIMED_TNT);
                entity.setFuseTicks(random.nextInt(16) + 200);

            }).run();
        });
    }

    public static void mine() {
        MessageUtils.broadcastChat("挖石头掉矿率提升了5倍");
        IsletopiaSkyWar.getGameInstance().mineBonus *= 5;

    }

    @SuppressWarnings("all")
    public static void enchantment() {
        MessageUtils.broadcastChat("所有玩家装备被附魔");
        ScheduleUtils.runSync(() -> {
            for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                if (!player.isOnline()) {
                    continue;
                }
                for (ItemStack armorContent : player.getInventory().getArmorContents()) {
                    if (armorContent == null) {
                        continue;
                    }
                    armorContent.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, new Random().nextInt(5));
                    armorContent.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, new Random().nextInt(5));
                    armorContent.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, new Random().nextInt(5));
                    armorContent.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, new Random().nextInt(5));
                    armorContent.addUnsafeEnchantment(Enchantment.DURABILITY, new Random().nextInt(10));
                }
            }
        });

    }

    public static void wither() {
        MessageUtils.broadcastChat("中心岛屿生成了凋灵");
        ScheduleUtils.runSync(() -> {
            Block randomSafeLand = IslandManager.getIslandRegion(0).getRandomSafeLand(100);
            Location add = randomSafeLand.getLocation().add(0, 30, 0);
            add.getWorld().spawnEntity(add, EntityType.WITHER);
        });
    }

    public static void end(){
        MessageUtils.broadcastChat("区块生物群戏将变为末地");
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
                        chunk.getBlock(i, k, j).setBiome(Biome.THE_END);
                    }
                }
            }

        }).run();
    }

    public static void nether(){
        MessageUtils.broadcastChat("区块生物群戏将变为下界");
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
                        chunk.getBlock(i, k, j).setBiome(Biome.NETHER_WASTES);
                    }
                }
            }

        }).run();
    }

    public static void upgrade(){
        MessageUtils.broadcastChat("所有玩家无限经验");

        ScheduleUtils.runSync(() -> {
            for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                player.giveExp(10000000);
            }
        });
    }

    public static void night(){
        MessageUtils.broadcastChat("永夜模式已开启");
        ScheduleUtils.runSync(() -> {
            World world = Bukkit.getWorld("world");
            assert world != null;
            world.setTime(18000);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        });

    }

    public static  void lightning() {
        MessageUtils.broadcastChat("永久雷霆已开启");
        ScheduleUtils.runSync(() -> {
            World world = Bukkit.getWorld("world");
            assert world != null;
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
            world.setThundering(true);
            Random random = new Random();
            for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                if (random.nextBoolean() && random.nextBoolean()) {
                    world.strikeLightning(player.getLocation());
                }
            }

        });
    }
}
