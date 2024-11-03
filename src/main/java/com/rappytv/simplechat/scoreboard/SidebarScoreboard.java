package com.rappytv.simplechat.scoreboard;

import com.google.common.collect.Lists;
import com.rappytv.simplechat.SimpleChat;
import net.funoase.sahara.bukkit.scoreboard.ScoreboardBuilder;
import net.funoase.sahara.bukkit.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SidebarScoreboard extends ScoreboardBuilder {

    private static SimpleChat plugin;
    private BukkitRunnable runnable;

    public SidebarScoreboard(Player p) {
        super(p, Colors.translateCodes(SimpleChat.setPlaceholders(p, plugin.i18n().translate("scoreboard.title"))));

        int updateInterval = plugin.getConfig().getInt("i18n.scoreboard.updateInterval");
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
        List<String> scores = Lists.reverse(plugin.getConfig().getStringList("i18n.scoreboard.scores"));
        for(int i = 0; i < scores.size(); i++) {
            setScore(Colors.translateCodes(SimpleChat.setPlaceholders(player, scores.get(i))), i);
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