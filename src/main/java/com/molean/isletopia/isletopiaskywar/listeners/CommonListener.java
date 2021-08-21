package com.molean.isletopia.isletopiaskywar.listeners;

import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PiglinBarterEvent;

public class CommonListener implements Listener {
    public CommonListener() {
        Bukkit.getPluginManager().registerEvents(this, IsletopiaSkyWar.getPlugin(IsletopiaSkyWar.class));

    }

    @EventHandler
    public void on(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Piglin piglin) {
            piglin.setImmuneToZombification(true);
        }
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (event.getEntity() instanceof Horse horse) {
            event.setDamage(event.getDamage() * 0.05);
        }
    }
}
