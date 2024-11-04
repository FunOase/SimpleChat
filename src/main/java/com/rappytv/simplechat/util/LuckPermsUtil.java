package com.rappytv.simplechat.util;

import com.rappytv.simplechat.SimpleChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

@SuppressWarnings("ConstantConditions")
public class LuckPermsUtil {

    private final static MiniMessage minimessage = MiniMessage.miniMessage();
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

    @Nullable
    public String getSavedColor(PermissionHolder holder) {
        return holder.getCachedData().getMetaData().getMetaValue("name_color");
    }

    public String getPrefix(Player player) {
        boolean preferUserMetaData = plugin.getConfig().getBoolean("prefer_user_metadata");
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
        boolean preferUserMetaData = plugin.getConfig().getBoolean("prefer_user_metadata");
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

    private Component getTabPrefix(Player player) {
        String format = plugin.getConfig().getString("tab.prefix");
        return !format.isBlank() ? minimessage.deserialize(
                format,
                Placeholder.parsed("player_prefix", getPrefix(player))
        ) : Component.empty();
    }

    private Component getTabSuffix(Player player) {
        String format = plugin.getConfig().getString("tab.suffix");
        return !format.isBlank() ? minimessage.deserialize(
                format,
                Placeholder.parsed("player_suffix", getSuffix(player))
        ) : Component.empty();
    }

    public NamedTextColor getNameColor(Player player) {
        NamedTextColor defaultColor = NamedTextColor.NAMES.valueOr(
                plugin.getConfig().getString("tab.default_color").toLowerCase(),
                NamedTextColor.WHITE
        );

        boolean preferUserMetaData = plugin.getConfig().getBoolean("prefer_user_metadata");
        String playerColor = getSavedColor(getUser(player));
        String groupColor = getSavedColor(getPrimaryGroup(player));

        return NamedTextColor.NAMES.valueOr(
                preferUserMetaData
                    ? playerColor != null ? playerColor : groupColor
                    : groupColor != null ? groupColor : playerColor,
                defaultColor
        );
    }

    public void setTabPrefix(Player player) {
        Iterator<? extends Player> players = Bukkit.getOnlinePlayers().iterator();

        Scoreboard board = player.getScoreboard();
        Player target;
        String teamId;
        Team team;
        Component prefix;
        Component suffix;

        board.getTeams().forEach((t) -> {
            if(t.getEntries().isEmpty())
                t.unregister();
        });
        while(players.hasNext()) {
            target = players.next();
            teamId = getTeamId(target);
            team = board.getTeam(teamId);
            if(team == null) {
                team = board.registerNewTeam(teamId);
            }

            prefix = getTabPrefix(target);
            suffix = getTabSuffix(target);

            team.prefix(prefix);
            team.suffix(suffix);
            team.color(getNameColor(target));
            team.addEntry(target.getName());
        }

        players = Bukkit.getOnlinePlayers().iterator();

        while(players.hasNext()) {
            target = players.next();
            if (target != player) {
                board = target.getScoreboard();
                teamId = getTeamId(player);
                team = board.getTeam(teamId);
                if(team == null) {
                    team = board.registerNewTeam(teamId);
                }

                prefix = getTabPrefix(player);
                suffix = getTabSuffix(player);

                team.prefix(prefix);
                team.suffix(suffix);
                team.color(getNameColor(player));
                team.addEntry(player.getName());
            }
        }
    }

    private String getTeamId(Player player) {
        int maxWeight = plugin.getConfig().getInt("tab.max_weight");
        int maxWeightLength = Integer.toString(maxWeight).length();
        int weight = getPrimaryGroup(player).getWeight().isPresent() ? getPrimaryGroup(player).getWeight().getAsInt() : 0;

        String id = ("0000" + (maxWeight - weight));
        String slicedId = id.substring(id.length() - maxWeightLength);

        return String.format("%s_%s", slicedId, player.getUniqueId());
    }
}
