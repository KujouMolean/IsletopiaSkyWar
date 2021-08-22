package com.molean.isletopia.isletopiaskywar.utils;

import com.molean.isletopia.isletopiaskywar.IslandManager;
import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import com.molean.isletopia.isletopiaskywar.tasks.MassiveChunkTask;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class RandomEventUtils {

    public static final Map<String, Runnable> map = new HashMap<>();

    static {
        map.put("tntRain", () -> {
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
        });

        map.put("plains",() -> {
            MessageUtils.broadcastChat("区块生物群戏将变为平原(正常刷新怪物)");
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
                            chunk.getBlock(i, k, j).setBiome(Biome.PLAINS);
                        }
                    }
                }

            }).run();
        });

        map.put("mine", () -> {
            MessageUtils.broadcastChat("挖石头掉矿率提升了5倍");
            IsletopiaSkyWar.getGameInstance().mineBonus *= 5;
        });


                map.put("enchantment", () -> {
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
        });

        map.put("wither", () -> {
            MessageUtils.broadcastChat("中心岛屿生成了凋灵");
            ScheduleUtils.runSync(() -> {
                Block randomSafeLand = IslandManager.getIslandRegion(0).getRandomSafeLand(100);
                if (randomSafeLand == null) {
                    return;
                }
                Location add = randomSafeLand.getLocation().add(0, 30, 0);
                add.getWorld().spawnEntity(add, EntityType.WITHER);
            });
        });

       map.put("end" ,() -> {
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
        });

       map.put("nether" ,() -> {
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
        });

        map.put("upgrade ", () -> {
            MessageUtils.broadcastChat("所有玩家无限经验");

            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    player.giveExp(10000000);
                }
            });
        });

        map.put("night ", () -> {
            MessageUtils.broadcastChat("永夜模式已开启");
            ScheduleUtils.runSync(() -> {
                World world = Bukkit.getWorld("world");
                assert world != null;
                world.setTime(18000);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            });

        });

        map.put("lightning ", () -> {
            MessageUtils.broadcastChat("永久雷霆已开启");
            ScheduleUtils.runSync(() -> {
                World world = Bukkit.getWorld("world");
                assert world != null;
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.FALSE);
                world.setThundering(true);
                world.setThunderDuration(100);
                Random random = new Random();
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    if (random.nextBoolean() && random.nextBoolean()) {
                        world.strikeLightning(player.getLocation());
                    }
                }

            });
        });

        map.put("starve", () -> {
            MessageUtils.broadcastChat("饥荒");
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20 * 60, 0));
                }
            });
        });

        map.put("brute", () -> {
            MessageUtils.broadcastChat("中心岛屿随机出现3只猪灵蛮兵");
            ScheduleUtils.runSync(() -> {
                for (int i = 0; i < 3; i++) {
                    Block randomSafeLand = IslandManager.getIslandRegion(0).getRandomSafeLand(100);
                    if (randomSafeLand == null) {
                        return;
                    }
                    Location add = randomSafeLand.getLocation().add(0, 2, 0);
                    add.getWorld().spawnEntity(add, EntityType.PIGLIN_BRUTE);
                }
            });
        });

        map.put("zombie", () -> {
            MessageUtils.broadcastChat("每个小岛生成5只僵尸");
            ScheduleUtils.runSync(() -> {
                for (int i = 1; i <= 8; i++) {
                    for (int j = 0; j < 5; j++) {
                        Block randomSafeLand = IslandManager.getIslandRegion(i).getRandomSafeLand(100);
                        if (randomSafeLand == null) {
                            return;
                        }
                        Location add = randomSafeLand.getLocation().add(0, 2, 0);
                        add.getWorld().spawnEntity(add, EntityType.ZOMBIE);
                    }

                }
            });
        });
        map.put("skeleton", () -> {
            MessageUtils.broadcastChat("每个小岛生成5只骷髅");
            ScheduleUtils.runSync(() -> {
                for (int i = 1; i <= 8; i++) {
                    for (int j = 0; j < 5; j++) {
                        Block randomSafeLand = IslandManager.getIslandRegion(i).getRandomSafeLand(100);
                        if (randomSafeLand == null) {
                            return;
                        }
                        Location add = randomSafeLand.getLocation().add(0, 2, 0);
                        add.getWorld().spawnEntity(add, EntityType.SKELETON);
                    }

                }
            });
        });
        map.put("raid", () -> {
            MessageUtils.broadcastChat("玩家获得灾厄buff");
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 20 * 6000, 0));
                }
            });
        });

        map.put("heal", () -> {
            MessageUtils.broadcastChat("玩家将恢复生命和饱食度");
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    player.setHealth(20);
                    player.setFoodLevel(20);
                }
            });
        });



        map.put("more", () -> {
            MessageUtils.broadcastChat("玩家背包所有物品数量提升至上限");
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    if (!player.isOnline()) {
                        continue;
                    }
                    for (ItemStack itemStack : player.getInventory()) {
                        if (itemStack == null) {
                            continue;
                        }
                        int maxStackSize = itemStack.getType().getMaxStackSize();
                        if (itemStack.getAmount() < maxStackSize) {
                            itemStack.setAmount(maxStackSize);
                        }
                    }
                }
            });
        });

        map.put("harvest", () -> {
            MessageUtils.broadcastChat("所有玩家获得零食礼包");
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    if (!player.isOnline()) {
                        continue;
                    }
                    HashMap<Integer, ItemStack> integerItemStackHashMap = player.getInventory().addItem(
                            new ItemStack(Material.CAKE),
                            new ItemStack(Material.BREAD),
                            new ItemStack(Material.CARROT),
                            new ItemStack(Material.BAKED_POTATO),
                            new ItemStack(Material.COOKIE));
                    for (ItemStack value : integerItemStackHashMap.values()) {
                        player.getLocation().getWorld().dropItem(player.getLocation(), value);
                    }
                }
            });
        });

        map.put("hot", () -> {
            MessageUtils.broadcastChat("所有玩家脚下生成岩浆块");
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    if (!player.isOnline()) {
                        continue;
                    }
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            player.getLocation().clone().add(i, -1, j).getBlock().setType(Material.MAGMA_BLOCK);
                        }
                    }
                }
            });
        });

        map.put("phantom ", () -> {
            MessageUtils.broadcastChat("每个岛生成3只幻翼");
            ScheduleUtils.runSync(() -> {
                for (int i = 1; i <= 8; i++) {
                    for (int j = 0; j < 3; j++) {
                        Block randomSafeLand = IslandManager.getIslandRegion(i).getRandomSafeLand(100);
                        if (randomSafeLand == null) {
                            return;
                        }
                        Location add = randomSafeLand.getLocation().add(0, 100, 0);
                        add.getWorld().spawnEntity(add, EntityType.PHANTOM);
                    }

                }
            });
        });
        map.put("creeper ", () -> {
            MessageUtils.broadcastChat("每个岛生成5只苦力怕");
            ScheduleUtils.runSync(() -> {
                for (int i = 1; i <= 8; i++) {
                    for (int j = 0; j < 5; j++) {
                        Block randomSafeLand = IslandManager.getIslandRegion(i).getRandomSafeLand(100);
                        if (randomSafeLand == null) {
                            return;
                        }
                        Location add = randomSafeLand.getLocation().add(0, 2, 0);
                        add.getWorld().spawnEntity(add, EntityType.CREEPER);
                    }

                }
            });
        });

        map.put("superpowers ", () -> {
            MessageUtils.broadcastChat("玩家获得永久速度+跳跃");
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 6000, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 6000, 1));

                }
            });
        });

        map.put("chicken ", () -> {
            MessageUtils.broadcastChat("鸡你太美");
            Random random = new Random();
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    for (int i = 0; i < random.nextInt(10); i++) {
                        Location add = player.getLocation().add(random.nextInt(5), 0, random.nextInt(5));
                        add.getWorld().spawnEntity(add, EntityType.CHICKEN);
                    }

                }
            });
        });
        map.put("elytra ", () -> {
            MessageUtils.broadcastChat("所有玩家获得鞘翅");
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    ItemStack itemStack = new ItemStack(Material.ELYTRA);
                    itemStack.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
                    player.getInventory().setChestplate(itemStack);
                }
            });
        });
        map.put("frozen ", () -> {
            MessageUtils.broadcastChat("所有玩家被冰冻");
            BlockData blockData = Bukkit.createBlockData(Material.ICE);
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    for (int i = -2; i <= 2; i++) {
                        for (int j = -2; j <= 2; j++) {
                            for (int k = -2; k <= 2; k++) {
                                Location location = player.getLocation().clone().add(i, j, k);
                                player.sendBlockChange(location, blockData);
                            }
                        }
                    }
                }
            });
        });

        map.put("glow ", () -> {
            MessageUtils.broadcastChat("所有玩家发光");
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 6000, 0));

                }
            });
        });


        map.put("power ", () -> {
            MessageUtils.broadcastChat("所有玩家获得力量II");
            ScheduleUtils.runSync(() -> {
                for (Player player : IsletopiaSkyWar.getGameInstance().gamePlayers.keySet()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 6000, 1));
                }
            });
        });
    }


}
