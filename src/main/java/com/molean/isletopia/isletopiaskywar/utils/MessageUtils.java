package com.molean.isletopia.isletopiaskywar.utils;

import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public enum MessageUtils {
    INSTANCE;

    MessageUtils() {
//        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(IsletopiaSkyWar.class), () -> {
//            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
//                onlinePlayer.sendMessage("");
//            }
//        }, 20L, 20L);
    }

    public static void broadcastChat(String message) {
        Bukkit.broadcast(Component.text(message));
    }
}
