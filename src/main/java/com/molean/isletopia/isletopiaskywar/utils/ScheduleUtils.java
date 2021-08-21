package com.molean.isletopia.isletopiaskywar.utils;

import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ScheduleUtils {

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(IsletopiaSkyWar.class), runnable);
    }

    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(IsletopiaSkyWar.class), runnable);
    }

}
