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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

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

    @NotNull
    public Group getPrimaryGroup(Player player) {
        User user = getUser(player);
        Group primaryGroup = plugin.lp.getGroupManager().getGroup(user.getPrimaryGroup());

        return primaryGroup != null ? primaryGroup : plugin.lp.getGroupManager().getGroup("default");
    }

    @Nullable
    public String getSavedColor(PermissionHolder holder) {
        return holder.getCachedData().getMetaData().getMetaValue("name_color");
    }

    @NotNull
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

    @NotNull
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

    /**
     * Get a player's name color
     * @param player The player you want to get the name color of
     * @return The player's {@link NamedTextColor}
     */
    @NotNull
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

    /**
     * Load the scoreboard for the player. This needs to be executed synchronously
     * @param player The player you want to load the scoreboard for
     */
    public void setTabPrefix(Player player) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        Scoreboard board = player.getScoreboard();
        String teamId;
        Team team;

        board.getTeams().forEach((t) -> {
            if(t.getEntries().isEmpty())
                t.unregister();
        });
        // Add all players to own scoreboard
        for(Player target : players) {
            teamId = getTeamId(target);
            team = board.getTeam(teamId);
            if(team == null) {
                team = board.registerNewTeam(teamId);
            }

            team.prefix(getTabPrefix(target));
            team.suffix(getTabSuffix(target));
            team.color(getNameColor(target));
            team.addEntry(target.getName());
        }

        // Add player to scoreboard of other players
        String name = player.getName();
        Component prefix = getTabPrefix(player);
        Component suffix = getTabSuffix(player);
        NamedTextColor color = getNameColor(player);

        for(Player target : players) {
            if (target != player) {
                board = target.getScoreboard();
                board.getTeams().forEach((t) -> {
                    if(t.getEntries().isEmpty())
                        t.unregister();
                });
                teamId = getTeamId(player);
                team = board.getTeam(teamId);
                if(team == null) {
                    team = board.registerNewTeam(teamId);
                }

                team.prefix(prefix);
                team.suffix(suffix);
                team.color(color);
                team.addEntry(name);
            }
        }
    }

    private String getTeamId(Player player) {
        int maxWeight = plugin.getConfig().getInt("tab.max_weight");
        int maxWeightLength = Integer.toString(maxWeight).length();
        int weight = getPrimaryGroup(player).getWeight().orElse(0);

        String id = ("0000" + (maxWeight - weight));
        String slicedId = id.substring(id.length() - maxWeightLength);

        return String.format("%s_%s", slicedId, player.getUniqueId());
    }
}
