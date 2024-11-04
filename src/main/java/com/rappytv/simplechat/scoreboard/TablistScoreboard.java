package com.rappytv.simplechat.scoreboard;

import com.rappytv.simplechat.SimpleChat;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TablistScoreboard {

    private final Player player;
    private static SimpleChat plugin;
    private BukkitRunnable runnable;

    public TablistScoreboard(Player player) {
        this.player = player;

        update();
        int updateInterval = plugin.getConfig().getInt("tab.updateInterval");
        if(updateInterval != -1) run(updateInterval);
    }

    public static void init(SimpleChat plugin) {
        TablistScoreboard.plugin = plugin;
        for(Player player : Bukkit.getOnlinePlayers()) {
            new TablistScoreboard(player);
        }
    }

    public void update() {
        player.sendPlayerListHeaderAndFooter(
                MiniMessage.miniMessage().deserialize(SimpleChat.setPlaceholders(
                        player,
                        plugin.getConfig().getString("tab.header")
                )),
                MiniMessage.miniMessage().deserialize(SimpleChat.setPlaceholders(
                        player,
                        plugin.getConfig().getString("tab.footer")
                ))
        );
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