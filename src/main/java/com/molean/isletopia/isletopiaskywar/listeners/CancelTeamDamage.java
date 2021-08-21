package com.molean.isletopia.isletopiaskywar.listeners;

import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class CancelTeamDamage implements Listener {

    public CancelTeamDamage() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(IsletopiaSkyWar.class));
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        if (!(damager instanceof Player damagerPlayer)) {
            return;
        }
        if (!(entity instanceof Player damagedPlayer)) {
            return;
        }

        Integer group1 = IsletopiaSkyWar.getGameInstance().gamePlayers.get(damagedPlayer);
        Integer group2 = IsletopiaSkyWar.getGameInstance().gamePlayers.get(damagerPlayer);

        if (Objects.equals(group1, group2)) {
            event.setCancelled(true);
        }
    }
}
