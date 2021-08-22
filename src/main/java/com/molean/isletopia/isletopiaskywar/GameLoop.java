package com.molean.isletopia.isletopiaskywar;

import com.molean.isletopia.isletopiaskywar.objects.CuboidShape;
import com.molean.isletopia.isletopiaskywar.tasks.IslandGenerateTask;
import com.molean.isletopia.isletopiaskywar.utils.*;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class GameLoop {
    private final static Random RANDOM = new Random();

    public static int countAvailablePlayer() {
        int count = 0;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.isOp()) {
                count++;
            }
        }
        return count;
    }

    public static boolean isGameOver() {
        GameInstance gameInstance = IsletopiaSkyWar.getGameInstance();
        Collection<Integer> values = gameInstance.gamePlayers.values();
        if (values.size() <= 1) {
            return true;
        }
        int i = 0;
        for (Integer value : values) {
            if (i == 0) {
                i = value;
            } else {
                if (value != i) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean waiting = false;
    private static Integer resource = 0;

    public static void asyncLoop() {
        World world = Bukkit.getWorld("world");
        assert world != null;
        //wait 8 players and then wait 1 minutes

        IsletopiaSkyWar.setGameInstance(new GameInstance());

        GameInstance gameInstance = IsletopiaSkyWar.getGameInstance();

        gameInstance.gameStatus = GameInstance.GameStatus.WAITING;
        int waitTime = 0;
        while (countAvailablePlayer() < 8) {
            if (waitTime % 15 == 0) {
                MessageUtils.broadcastChat("至少8位玩家才能开始游戏, 正在等待玩家加入...");
                MessageUtils.broadcastChat("目前已有" + countAvailablePlayer());

            }
            waitTime++;
            ThreadUtils.sleepSecond();
        }

        MessageUtils.broadcastChat("已达8位玩家, 正在准备游戏地图....");
        MessageUtils.broadcastChat("在游戏地图生成期间, 可以使用指令组队: /team x (x为1~8的数字)");


        //reset game


        MessageUtils.broadcastChat("正在清空地图...");
        //clear region
        waiting = true;


        IslandManager.clear(4, () -> {
            waiting = false;
        });


        while (waiting) {
            ThreadUtils.sleepSecond();
        }


        //generate map


        //*generate central island
        MessageUtils.broadcastChat("正在生成中心岛...");

        waiting = true;
        final Biome outerBiome = IslandGenerationUtils.getRandomBiome();

        new IslandGenerateTask(outerBiome, 128, 2000, outerBlocks -> {
            IslandManager.getIslandRegion(0).applyCenter(new CuboidShape(outerBlocks, outerBiome), 2000, () -> {
                waiting = false;
            });
        }).run();

        while (waiting) {
            ThreadUtils.sleepSecond();
        }


        //*generate 8 small island

        MessageUtils.broadcastChat("正在生成四周小岛...");

        resource = 8;
        final Biome innerBiome = IslandGenerationUtils.getRandomBiome();
        for (int i = 1; i <= 8; i++) {
            final int finalI = i;
            new IslandGenerateTask(innerBiome, 64, 500, innerBlocks -> {
                IslandManager.getIslandRegion(finalI).applyCenter(new CuboidShape(innerBlocks, innerBiome), 100, () -> {
                    resource--;
                });
            }).run();
        }
        while (resource > 0) {
            ThreadUtils.sleepSecond();
        }


        //set world env

        ScheduleUtils.runSync(() -> {

            world.getWorldBorder().setCenter(255, 255);
            world.getWorldBorder().setSize(512);
            world.setDifficulty(Difficulty.EASY);

            world.setGameRule(GameRule.KEEP_INVENTORY, false);
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 30);
            world.setGameRule(GameRule.DO_INSOMNIA, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setTime(1200);

            ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
            Bukkit.dispatchCommand(consoleSender, "weather clear");
        });

        MessageUtils.broadcastChat("岛屿生成完毕,准备开始游戏...");
        //allocation group

        ArrayList<Player> players = new ArrayList<>();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.isOp()) {
                players.add(onlinePlayer);
            }
        }


        //kick full out player
        while (players.size() > 32) {
            players.remove(new Random().nextInt(players.size()));
        }


        gameInstance.gamePlayers = TeamSystem.group(players);

        // teleport players to their island

        ScheduleUtils.runSync(() -> {
            gameInstance.gamePlayers.forEach((player, integer) -> {
                Block randomSafeLand = IslandManager.getIslandRegion(integer).getRandomSafeLand(100);
                if (randomSafeLand == null) {
                    return;
                }
                player.teleport(randomSafeLand.getLocation().add(0, 2, 0));
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setExp(0);
                player.displayName(Component.text("§f(§2" + integer + "§f)" + player.getName()));
                player.sendTitle("§2游戏开始", "§e和你的队友一起活到最后", 0, 40, 0);
                for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
                    player.removePotionEffect(activePotionEffect.getType());
                }

            });
        });


        MessageUtils.broadcastChat("§c游戏中的玩家自动进入队伍聊天模式,如果有需要,请以#开头使用全局对话");

        MessageUtils.broadcastChat("游戏开始");

        gameInstance.gameStatus = GameInstance.GameStatus.STAGE_1;
        // stage 1 initial
        MessageUtils.broadcastChat("第一阶段开始");
        {
            List<ItemStack> itemStacks = ItemGeneratorUtils.generateStage1();

            ScheduleUtils.runSync(() -> {
                for (int i = 1; i <= 8; i++) {
                    ArrayList<Inventory> inventories = new ArrayList<>();
                    for (int j = 0; j < 4; j++) {
                        Block randomSafeLand = IslandManager.getIslandRegion(i).getRandomSafeLand(100);
                        if (randomSafeLand == null) {
                            continue;
                        }
                        Block relative = randomSafeLand.getRelative(BlockFace.UP);
                        relative.setType(Material.CHEST);
                        Chest chest = (Chest) relative.getState();
                        inventories.add(chest.getInventory());
                    }
                    ChestUtils.randomlySplitItem(inventories, itemStacks);
                }
                MessageUtils.broadcastChat("箱子已经生成完毕");
            });
        }
        // stage 1 waiting

        for (int i = 0; i < 60 * 10; i++) {
            ThreadUtils.sleepSecond();

            if (isGameOver()) {
                break;
            }
            if (i % 60 == 0) {
                MessageUtils.broadcastChat("距离第一阶段结束还有:" + (60 * 10 - i) + "秒");
            }
            if (gameInstance.gameStatus != GameInstance.GameStatus.STAGE_1) {
                break;
            }

        }
        gameInstance.gameStatus = GameInstance.GameStatus.STAGE_2;
        // stage 2 waiting

        MessageUtils.broadcastChat("开启第二阶段");
        int stageTwoWaitTime = 0;
        for (int i = 0; i < 60 * 10; i++) {
            if (isGameOver()) {
                break;
            }
            if (gameInstance.gameStatus != GameInstance.GameStatus.STAGE_2) {
                break;
            }
            if (stageTwoWaitTime % 60 == 0) {
                if (RANDOM.nextBoolean()) {

                    Collection<Runnable> values = RandomEventUtils.map.values();
                    Runnable runnable = new ArrayList<>(values).get(RANDOM.nextInt(values.size()));
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if (i % 60 == 0) {
                        MessageUtils.broadcastChat("什么也没发生, 距离第二阶段结束还有:" + (60 * 10 - i) + "秒");
                    }
                }
            }
            ThreadUtils.sleepSecond();

            stageTwoWaitTime++;
        }


        gameInstance.gameStatus = GameInstance.GameStatus.STAGE_3;
        //stage 3 initial

        MessageUtils.broadcastChat("第三阶段开始");

        //stage 3 waiting
        for (int i = 0; i < 60 * 15; i++) {
            ThreadUtils.sleepSecond();
            if (isGameOver()) {
                break;
            }
            if (gameInstance.gameStatus != GameInstance.GameStatus.STAGE_3) {
                break;
            }
            if (i % 60 == 0) {
                MessageUtils.broadcastChat("距离第三阶段结束还有:" + (60 * 15 - i) + "秒");
            }


            if (i % 180 == 0) {
                ScheduleUtils.runSync(() -> {
                    {
                        for (int j = 0; j < 8; j++) {
                            Block randomSafeLand = IslandManager.getIslandRegion(0).getRandomSafeLand(100);
                            if (randomSafeLand == null) {
                                continue;
                            }
                            Block relative = randomSafeLand.getRelative(BlockFace.UP);
                            relative.setType(Material.SHULKER_BOX);
                            ShulkerBox shulkerBox = (ShulkerBox) relative.getState();
                            ArrayList<Inventory> inventories = new ArrayList<>();
                            inventories.add(shulkerBox.getInventory());
                            ChestUtils.randomlySplitItem(inventories, ItemGeneratorUtils.generateStage3());
                        }
                    }
                    for (int j = 1; j <= 8; j++) {
                        Block randomSafeLand = IslandManager.getIslandRegion(j).getRandomSafeLand(100);
                        if (randomSafeLand == null) {
                            continue;
                        }
                        Block relative = randomSafeLand.getRelative(BlockFace.UP);
                        relative.setType(Material.SHULKER_BOX);
                        ShulkerBox shulkerBox = (ShulkerBox) relative.getState();
                        ArrayList<Inventory> inventories = new ArrayList<>();
                        inventories.add(shulkerBox.getInventory());
                        ChestUtils.randomlySplitItem(inventories, ItemGeneratorUtils.generateStage2());

                    }
                    MessageUtils.broadcastChat("箱子已经生成完毕");
                });
            }
        }
        gameInstance.gameStatus = GameInstance.GameStatus.STAGE_4;
        //stage 4
        MessageUtils.broadcastChat("第四阶段开始");
        MessageUtils.broadcastChat("每秒缩圈0.4格");
        for (int i = 0; i < 24 * 60 * 60; i++) {
            ThreadUtils.sleepSecond();
            if (isGameOver()) {
                break;
            }
            if (gameInstance.gameStatus != GameInstance.GameStatus.STAGE_4) {
                break;
            }
            if (i % 60 == 0) {
                MessageUtils.broadcastChat("第四阶段已经过去了" + i + "秒");
                MessageUtils.broadcastChat("剩余玩家:" + IsletopiaSkyWar.getGameInstance().gamePlayers.keySet().size());
            }
            int finalI = i;
            ScheduleUtils.runSync(() -> {
                world.getWorldBorder().setSize(512 - finalI * 0.4);
            });


        }


        gameInstance.gameStatus = GameInstance.GameStatus.ENDING;
        // gameover
        MessageUtils.broadcastChat("游戏结束");

        HashSet<String> strings = new HashSet<>();

        for (Player player : gameInstance.gamePlayers.keySet()) {
            strings.add(player.getName());
        }
        String join = String.join(",", strings);

        MessageUtils.broadcastChat(join + "获得胜利");

        //game start
    }

}
