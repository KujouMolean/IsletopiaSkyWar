package com.molean.isletopia.isletopiaskywar.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PrepareEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();


    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
