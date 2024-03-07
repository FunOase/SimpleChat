package com.rappytv.chat.events;

import com.rappytv.chat.RyChat;
import com.rappytv.chat.scoreboard.SidebarScoreboard;
import com.rappytv.chat.scoreboard.TablistScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final RyChat plugin;

    public JoinListener(RyChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(plugin.getConfig().getBoolean("i18n.tab.enabled")) new TablistScoreboard(player);
        if(plugin.getConfig().getBoolean("i18n.scoreboard.enabled")) new SidebarScoreboard(player);
        plugin.getLuckPermsUtil().setTabPrefix(player);
    }
}
