package com.rappytv.chat.util;

import com.rappytv.chat.ChatPlugin;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;

@SuppressWarnings("ConstantConditions")
public class LuckPermsUtil {

    private final ChatPlugin plugin;

    public LuckPermsUtil(ChatPlugin plugin) {
        this.plugin = plugin;
    }

    private User getUser(Player player) {
        return plugin.lp.getPlayerAdapter(Player.class).getUser(player);
    }

    public Group getPrimaryGroup(Player player) {
        User user = getUser(player);
        return plugin.lp.getGroupManager().getGroup(user.getPrimaryGroup());
    }

    public String getPrefix(Player player) {
        Group group = getPrimaryGroup(player);
        CachedMetaData meta = group.getCachedData().getMetaData();

        return meta.getPrefix() != null ? meta.getPrefix() : "";
    }

    public String getSuffix(Player player) {
        Group group = getPrimaryGroup(player);
        CachedMetaData meta = group.getCachedData().getMetaData();

        return meta.getSuffix() != null ? meta.getSuffix() : "";
    }

    private String getTabPrefix(Player player) {
        if(!plugin.getConfig().contains("format.tab.prefix")) {
            plugin.getLogger().severe("Tab prefix has to be set!");
            return "";
        }
        String prefix = getPrefix(player);
        return prefix.isEmpty() ? "" : ChatColor.translateAlternateColorCodes(
                '&',
                plugin
                        .getConfig()
                        .getString("format.tab.prefix")
                        .replaceAll("<prefix>", prefix)
        );
    }

    private String getTabSuffix(Player player) {
        if(!plugin.getConfig().contains("format.tab.suffix")) {
            plugin.getLogger().severe("Tab suffix has to be set!");
            return "";
        }
        String suffix = getSuffix(player);
        return suffix.isEmpty() ? "" : ChatColor.translateAlternateColorCodes(
                '&',
                plugin
                        .getConfig()
                        .getString("format.tab.suffix")
                        .replaceAll("<suffix>", suffix)
        );
    }

    private ChatColor getNameColor() {
        if(!plugin.getConfig().contains("format.tab.color")) {
            plugin.getLogger().severe("Tab name color has to be set!");
            return ChatColor.WHITE;
        }
        try {
            return ChatColor.valueOf(plugin.getConfig().getString("format.tab.color"));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().severe("Invalid tab name color!");
            return ChatColor.WHITE;
        }
    }

    public void setTabPrefix(Player player) {
        Iterator<? extends Player> players = Bukkit.getOnlinePlayers().iterator();

        Scoreboard sb = player.getScoreboard();
        Player target;
        String teamId;
        Team team;
        String prefix;
        String suffix;

        sb.getTeams().forEach((t) -> {
            if(t.getEntries().isEmpty())
                t.unregister();
        });
        while(players.hasNext()) {
            target = players.next();
            teamId = getTeamId(target);
            team = sb.getTeam(teamId);
            if(team == null) {
                team = sb.registerNewTeam(teamId);
            }

            prefix = getTabPrefix(target);
            suffix = getTabSuffix(target);

            if(!prefix.isEmpty()) team.setPrefix(prefix);
            if(!suffix.isEmpty()) team.setSuffix(suffix);
            team.setColor(getNameColor());
            team.addEntry(target.getName());
        }

        players = Bukkit.getOnlinePlayers().iterator();

        while(players.hasNext()) {
            target = players.next();
            if (target != player) {
                sb = target.getScoreboard();
                teamId = getTeamId(player);
                team = sb.getTeam(teamId);
                if(team == null) {
                    team = sb.registerNewTeam(teamId);
                }

                prefix = getTabPrefix(player);
                suffix = getTabSuffix(player);

                if(!prefix.isEmpty()) team.setPrefix(prefix);
                if(!suffix.isEmpty()) team.setSuffix(suffix);
                team.setColor(getNameColor());
                team.addEntry(player.getName());
            }
        }
    }

    private String getTeamId(Player player) {
        int maxWeight = plugin.getConfig().getInt("maxWeight");
        int maxWeightLength = Integer.toString(maxWeight).length();
        int weight = getPrimaryGroup(player).getWeight().isPresent() ? getPrimaryGroup(player).getWeight().getAsInt() : 0;

        String id = ("0000" + (maxWeight - weight));
        String slicedId = id.substring(id.length() - maxWeightLength);

        return String.format("%s_%s", slicedId, player.getUniqueId());
    }
}
