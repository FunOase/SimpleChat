package com.rappytv.chat.events;

import com.rappytv.chat.util.PlayerConverter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerConverter.setTabPrefix(event.getPlayer());
    }
}
