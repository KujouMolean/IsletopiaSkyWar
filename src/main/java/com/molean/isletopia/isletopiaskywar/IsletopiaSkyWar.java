package com.molean.isletopia.isletopiaskywar;

import com.molean.isletopia.isletopiaskywar.command.RandomEventTestCommand;
import com.molean.isletopia.isletopiaskywar.command.SDebugCommand;
import com.molean.isletopia.isletopiaskywar.command.TDebug;
import com.molean.isletopia.isletopiaskywar.command.TeamCommand;
import com.molean.isletopia.isletopiaskywar.listeners.CancelTeamDamage;
import com.molean.isletopia.isletopiaskywar.listeners.CommonListener;
import com.molean.isletopia.isletopiaskywar.listeners.MineModification;
import com.molean.isletopia.isletopiaskywar.listeners.PlayerLeftEvent;
import com.molean.isletopia.isletopiaskywar.utils.IslandGenerationUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public final class IsletopiaSkyWar extends JavaPlugin implements Listener {


    private static GameInstance gameInstance = new GameInstance();

    public static GameInstance getGameInstance() {
        return gameInstance;
    }

    public static void setGameInstance(GameInstance gameInstance) {
        IsletopiaSkyWar.gameInstance = gameInstance;
    }

    @Override
    @SuppressWarnings("all")
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        new SDebugCommand();
        new RandomEventTestCommand();
        new TDebug();
        new CommonListener();
        new CancelTeamDamage();
        new MineModification();
        new PlayerLeftEvent();
        new TeamCommand();


        Bukkit.getScheduler().runTaskTimer(IsletopiaSkyWar.this, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.isOp()) {
                    continue;
                }
                if (gameInstance.gamePlayers.containsKey(onlinePlayer)) {
                    continue;
                }
                onlinePlayer.setGameMode(GameMode.SPECTATOR);

            }
        }, 20L, 20L);

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            while (true) {
                try {
                    GameLoop.asyncLoop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @EventHandler
    public void on(ServerLoadEvent event) {
        IslandGenerationUtils.init();
        World world = Bukkit.getWorld("world");
        assert world != null;
        world.getWorldBorder().setCenter(255, 255);
        world.getWorldBorder().setSize(512);
    }


    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
        return new EmptyChunkGenerator();
    }
}
