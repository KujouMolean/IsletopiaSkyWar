package com.molean.isletopia.isletopiaskywar.command;

import com.molean.isletopia.isletopiaskywar.GameInstance;
import com.molean.isletopia.isletopiaskywar.IslandManager;
import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
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

public class TDebug implements CommandExecutor {
    public TDebug() {
        Objects.requireNonNull(Bukkit.getPluginCommand("tdebug")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (IsletopiaSkyWar.getGameInstance().gameStatus) {
            case STAGE_1->{
                IsletopiaSkyWar.getGameInstance().gameStatus = GameInstance.GameStatus.STAGE_2;
            }
            case STAGE_2->{
                IsletopiaSkyWar.getGameInstance().gameStatus = GameInstance.GameStatus.STAGE_3;
            }
            case STAGE_3->{
                IsletopiaSkyWar.getGameInstance().gameStatus = GameInstance.GameStatus.STAGE_4;

            }
            case STAGE_4->{
                IsletopiaSkyWar.getGameInstance().gameStatus = GameInstance.GameStatus.ENDING;

            }
        }
        return true;
    }
}
