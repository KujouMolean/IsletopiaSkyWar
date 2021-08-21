package com.molean.isletopia.isletopiaskywar.listeners;

import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import com.molean.isletopia.isletopiaskywar.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerLeftEvent implements Listener {


    public PlayerLeftEvent() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(IsletopiaSkyWar.class));

    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        if (IsletopiaSkyWar.getGameInstance().gameStatus.isGaming()) {
            IsletopiaSkyWar.getGameInstance().gamePlayers.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        if (IsletopiaSkyWar.getGameInstance().gameStatus.isGaming()) {
            IsletopiaSkyWar.getGameInstance().gamePlayers.remove(event.getEntity());
            EntityDamageEvent lastDamageCause = event.getEntity().getLastDamageCause();
            if (lastDamageCause instanceof EntityDamageByEntityEvent entityDamageByEntityEvent) {
                Entity damager = entityDamageByEntityEvent.getDamager();
                if (damager instanceof Player player) {
                    MessageUtils.broadcastChat(event.getEntity().getName() + "被" + player.getName() + "杀了");
                } else {
                    MessageUtils.broadcastChat(event.getEntity().getName() + "死了");
                }
            } else {
                MessageUtils.broadcastChat(event.getEntity().getName() + "死了");
            }
        }
    }
}
