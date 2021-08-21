package com.molean.isletopia.isletopiaskywar.command;

import com.molean.isletopia.isletopiaskywar.TeamSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class TeamCommand implements CommandExecutor, TabCompleter {
    public TeamCommand() {
        Objects.requireNonNull(Bukkit.getPluginCommand("team")).setExecutor(this);
        Objects.requireNonNull(Bukkit.getPluginCommand("team")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage("用法: /team x (x为1~8的数字)");
            sender.sendMessage("例如: /team 3");
            return true;
        }

        int i;

        try {
            i = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("用法: /team x (x为1~8的数字)");
            sender.sendMessage("例如: /team 3");
            return true;
        }

        if (i <= 0 || i >= 9) {
            sender.sendMessage("用法: /team x (x为1~8的数字)");
            sender.sendMessage("例如: /team 3");
        }

        if (TeamSystem.requestGroup((Player) sender, i)) {
            sender.sendMessage("组队成功, 游戏开始后, 你将会被分配到: " + i + "号队伍");
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
