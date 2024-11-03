package com.rappytv.simplechat.util;

import com.rappytv.simplechat.SimpleChat;
import com.rappytv.rylib.util.Colors;
import com.rappytv.rylib.util.I18n;
import com.rappytv.rylib.util.Permissions;
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

    private final SimpleChat plugin;

    public LuckPermsUtil(SimpleChat plugin) {
        this.plugin = plugin;
    }

    private User getUser(Player player) {
        return plugin.lp.getPlayerAdapter(Player.class).getUser(player);
    }

    public Group getPrimaryGroup(Player player) {
        User user = getUser(player);
        Group primaryGroup = plugin.lp.getGroupManager().getGroup(user.getPrimaryGroup());

        return primaryGroup != null ? primaryGroup : plugin.lp.getGroupManager().getGroup("default");
    }

    public String getPrefix(Player player) {
        boolean preferUserMetaData = plugin.getConfig().getBoolean("preferUserMetaData");
        CachedMetaData playerData = getUser(player).getCachedData().getMetaData();
        CachedMetaData groupData = getPrimaryGroup(player).getCachedData().getMetaData();
        String prefix;

        if(preferUserMetaData) {
            if(playerData.getPrefix() != null) prefix = playerData.getPrefix();
            else if(groupData.getPrefix() != null) prefix = groupData.getPrefix();
            else prefix = "";
        } else {
            if(groupData.getPrefix() != null) prefix = groupData.getPrefix();
            else if(playerData.getPrefix() != null) prefix = playerData.getPrefix();
            else prefix = "";
        }

        return prefix;
    }

    public String getSuffix(Player player) {
        boolean preferUserMetaData = plugin.getConfig().getBoolean("preferUserMetaData");
        CachedMetaData playerData = getUser(player).getCachedData().getMetaData();
        CachedMetaData groupData = getPrimaryGroup(player).getCachedData().getMetaData();
        String suffix;

        if(preferUserMetaData) {
            if(playerData.getSuffix() != null) suffix = playerData.getSuffix();
            else if(groupData.getSuffix() != null) suffix = groupData.getSuffix();
            else suffix = "";
        } else {
            if(groupData.getSuffix() != null) suffix = groupData.getSuffix();
            else if(playerData.getSuffix() != null) suffix = playerData.getSuffix();
            else suffix = "";
        }

        return suffix;
    }

    private String getTabPrefix(Player player) {
        String prefix = Colors.translateCodes(SimpleChat.setPlaceholders(
                player,
                plugin.i18n().translate(
                        "tab.prefix",
                        new I18n.Argument("playerPrefix", getPrefix(player))
                )
        ));
        return prefix.isBlank() ? "" : prefix;
    }

    private String getTabSuffix(Player player) {
        String suffix = Colors.translateCodes(SimpleChat.setPlaceholders(
                player,
                plugin.i18n().translate(
                        "tab.suffix",
                        new I18n.Argument("playerSuffix", getSuffix(player))
                )
        ));
        return suffix.isBlank() ? "" : suffix;
    }

    private ChatColor getNameColor(Player player) {
        ChatColor defaultColor;
        try {
            defaultColor = ChatColor.valueOf(plugin.i18n().translate("tab.defaultColor").toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().severe("Invalid default tab color!");
            defaultColor = ChatColor.WHITE;
        }

        return Permissions.getEnumValue(
                player,
                "simplechat.name",
                defaultColor,
                ChatColor.class
        );
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

            team.setPrefix(prefix);
            team.setSuffix(suffix);
            team.setColor(getNameColor(target));
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

                team.setPrefix(prefix);
                team.setSuffix(suffix);
                team.setColor(getNameColor(player));
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
