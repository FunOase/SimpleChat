package com.rappytv.chat.events;

import com.rappytv.chat.Chat;
import com.rappytv.chat.util.LuckPermsUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final Chat plugin;

    public JoinListener(Chat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        plugin.getLuckPermsUtil().setTabPrefix(event.getPlayer());
    }
}
