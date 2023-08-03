package com.rappytv.chat.events;

import com.rappytv.chat.ChatPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final ChatPlugin plugin;

    public JoinListener(ChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        plugin.getLuckPermsUtil().setTabPrefix(event.getPlayer());
    }
}
