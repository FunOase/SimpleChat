package com.rappytv.simplechat.scoreboard;

import com.google.common.collect.Lists;
import com.rappytv.simplechat.SimpleChat;
import net.funoase.sahara.bukkit.scoreboard.ScoreboardBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SidebarScoreboard extends ScoreboardBuilder {

    private static SimpleChat plugin;
    private BukkitRunnable runnable;

    public SidebarScoreboard(Player player) {
        super(
                player,
                MiniMessage.miniMessage().deserialize(SimpleChat.setPlaceholders(
                        player,
                        plugin.getConfig().getString("scoreboard.title")
                ))
        );

        int updateInterval = plugin.getConfig().getInt("scoreboard.updateInterval");
        if(updateInterval != -1) run(updateInterval);
    }

    public static void init(SimpleChat plugin) {
        SidebarScoreboard.plugin = plugin;
        for(Player player : Bukkit.getOnlinePlayers()) {
            new SidebarScoreboard(player);
        }
    }

    @Override
    public void createScoreboard() {
        List<String> scores = Lists.reverse(plugin.getConfig().getStringList("scoreboard.scores"));
        for(int i = 0; i < scores.size(); i++) {
            setScore(MiniMessage.miniMessage().deserialize(SimpleChat.setPlaceholders(
                    player,
                    scores.get(i)
            )), i);
        }
    }

    private void run(int updateInterval) {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.isOnline()) {
                    runnable.cancel();
                    return;
                }
                createScoreboard();
            }
        };
        runnable.runTaskTimer(plugin, 0, 20L * updateInterval);
    }
}