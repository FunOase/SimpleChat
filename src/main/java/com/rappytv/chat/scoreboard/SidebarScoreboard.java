package com.rappytv.chat.scoreboard;

import com.google.common.collect.Lists;
import com.rappytv.chat.ChatPlugin;
import com.rappytv.rylib.scoreboard.ScoreboardBuilder;
import com.rappytv.rylib.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SidebarScoreboard extends ScoreboardBuilder {

    private static ChatPlugin plugin;
    private BukkitRunnable runnable;

    public SidebarScoreboard(Player p) {
        super(p, plugin.i18n().translate("scoreboard.title"));

        run();
    }

    public static void init(ChatPlugin plugin) {
        SidebarScoreboard.plugin = plugin;
        for(Player player : Bukkit.getOnlinePlayers()) {
            new SidebarScoreboard(player);
        }
    }

    @Override
    public void createScoreboard() {
        List<String> scores = Lists.reverse(plugin.getConfig().getStringList("i18n.scoreboard.scores"));
        for(int i = 0; i < scores.size(); i++) {
            setScore(ChatPlugin.setPlaceholders(player, Colors.translateCodes(scores.get(i))), i);
        }
    }

    private void run() {
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
        runnable.runTaskTimer(plugin, 0, 20 * 10);
    }
}