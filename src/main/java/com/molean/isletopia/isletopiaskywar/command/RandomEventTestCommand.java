package com.molean.isletopia.isletopiaskywar.command;

import com.molean.isletopia.isletopiaskywar.utils.RandomEventUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public class RandomEventTestCommand implements CommandExecutor {
    public RandomEventTestCommand() {
        Objects.requireNonNull(Bukkit.getPluginCommand("edebug")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "tnt" -> {
                RandomEventUtils.tntRain();
            }
            case "mine" -> {
                RandomEventUtils.mine();
            }
            case "enchantment" -> {
                RandomEventUtils.enchantment();
            }
            case "wither" -> {
                RandomEventUtils.wither();
            }
            case "end" -> {
                RandomEventUtils.end();
            }
            case "night" -> {
                RandomEventUtils.night();

            }
            case "nether" -> {
                RandomEventUtils.nether();
            }
            case "upgrade" -> {
                RandomEventUtils.upgrade();
            }
            case "lightning" -> {
                RandomEventUtils.lightning();
            }
        }

        return true;
    }
}
