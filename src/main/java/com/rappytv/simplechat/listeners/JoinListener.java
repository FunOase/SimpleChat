package com.rappytv.simplechat.listeners;

import com.rappytv.simplechat.SimpleChat;
import com.rappytv.simplechat.scoreboard.SidebarScoreboard;
import com.rappytv.simplechat.scoreboard.TablistScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final SimpleChat plugin;

    public JoinListener(SimpleChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(plugin.getConfig().getBoolean("tab.enabled")) new TablistScoreboard(player);
        if(plugin.getConfig().getBoolean("scoreboard.enabled")) new SidebarScoreboard(player);
        plugin.getLuckPermsUtil().setTabPrefix(player);
    }
}
