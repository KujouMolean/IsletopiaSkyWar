package com.molean.isletopia.isletopiaskywar.listeners;

import com.molean.isletopia.isletopiaskywar.IsletopiaSkyWar;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Set;

public class PlayerChatEvent implements Listener {
    public PlayerChatEvent() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(IsletopiaSkyWar.class));

    }

    @EventHandler
    @SuppressWarnings("all")
    public void on(AsyncPlayerChatEvent event) {
        Map<Player, Integer> gamePlayers = IsletopiaSkyWar.getGameInstance().gamePlayers;

        if (IsletopiaSkyWar.getGameInstance().gameStatus.isGaming()) {
            Player player = event.getPlayer();
            String message = event.getMessage();

            if (message.startsWith("#")) {
                event.setMessage(message.substring(1));
                return;
            }

            TextComponent textComponent = (TextComponent) player.displayName();
            String s = textComponent.content().replaceAll("§.", "");
            if (s.startsWith("(")) {
                event.setCancelled(true);
                String substring = s.substring(1, 2);
                try {
                    int i = Integer.parseInt(substring);
                    gamePlayers.forEach((player1, integer) ->{
                        if (integer == i) {
                            player1.sendMessage("§3" + player.getName() + ": " + message);
                        }
                    });

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.isOp()) {

                            onlinePlayer.sendMessage("§3" + ((TextComponent) player.displayName()).content() + "§3: " + message);
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
