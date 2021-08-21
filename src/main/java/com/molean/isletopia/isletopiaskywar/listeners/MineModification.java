package com.molean.isletopia.isletopiaskywar.listeners;

import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MineModification implements Listener {

    private final Map<Material, Integer> map = new HashMap<>();
    private final Random random = new Random();

    public MineModification() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(IsletopiaSkyWar.class));
        map.put(Material.EMERALD_ORE, 1);
        map.put(Material.DIAMOND_ORE, 2);
        map.put(Material.IRON_ORE, 3);
        map.put(Material.GOLD_ORE, 2);
        map.put(Material.LAPIS_ORE,2);
        map.put(Material.REDSTONE_ORE, 2);
        map.put(Material.COPPER_ORE, 1);
        map.put(Material.COAL_ORE, 5);

    }

    @EventHandler(ignoreCancelled = true)
    public void on(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(Material.STONE)) {
            return;
        }
        for (Material material : map.keySet()) {
            if (random.nextInt(100) < IsletopiaSkyWar.getGameInstance().mineBonus * map.get(material)) {
                event.getBlock().setType(material);
                break;
            }
        }
    }
}
