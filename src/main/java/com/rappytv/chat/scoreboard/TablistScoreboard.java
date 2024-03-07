package com.rappytv.chat.scoreboard;

import com.rappytv.chat.RyChat;
import com.rappytv.rylib.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TablistScoreboard {

    private final Player player;
    private static RyChat plugin;
    private BukkitRunnable runnable;

    public TablistScoreboard(Player player) {
        this.player = player;

        update();
        int updateInterval = plugin.getConfig().getInt("i18n.tab.updateInterval");
        if(updateInterval != -1) run(updateInterval);
    }

    public static void init(RyChat plugin) {
        TablistScoreboard.plugin = plugin;
        for(Player player : Bukkit.getOnlinePlayers()) {
            new TablistScoreboard(player);
        }
    }

    public void update() {
        String header = Colors.translateCodes(RyChat.setPlaceholders(player, plugin.i18n().translate("tab.header")));
        String footer = Colors.translateCodes(RyChat.setPlaceholders(player, plugin.i18n().translate("tab.footer")));

        player.setPlayerListHeader(header);
        player.setPlayerListFooter(footer);
    }

    private void run(int updateInterval) {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.isOnline()) {
                    runnable.cancel();
                    return;
                }
                update();
            }
        };
        runnable.runTaskTimer(plugin, 0, 20L * updateInterval);
    }
}