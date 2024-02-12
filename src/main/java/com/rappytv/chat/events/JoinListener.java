package com.rappytv.chat.events;

import com.rappytv.chat.ChatPlugin;
import com.rappytv.chat.scoreboard.SidebarScoreboard;
import com.rappytv.chat.scoreboard.TablistScoreboard;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        plugin.getLuckPermsUtil().setTabPrefix(player);
        new TablistScoreboard(player);
        new SidebarScoreboard(player);
    }
}
