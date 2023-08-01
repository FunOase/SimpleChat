package com.rappytv.chat.util;

import com.rappytv.chat.Chat;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;

@SuppressWarnings({"ConstantConditions", "deprecation"})
public class LuckPermsUtil {

    private final LuckPerms api;

    public LuckPermsUtil(LuckPerms api) {
        this.api = api;
    }

    private User getUser(Player player) {
        return api.getPlayerAdapter(Player.class).getUser(player);
    }

    private Group getPrimaryGroup(Player player) {
        User user = getUser(player);
        return api.getGroupManager().getGroup(user.getPrimaryGroup());
    }

    public String getChatPrefix(Player player) {
        Group group = getPrimaryGroup(player);
        if(group.getDisplayName() == null) return null;

        return ChatColor.translateAlternateColorCodes('&', group.getDisplayName());
    }

    public void setTabPrefix(Player player) {
        Iterator<? extends Player> players = Bukkit.getOnlinePlayers().iterator();

        Scoreboard sb = player.getScoreboard();
        Player target;
        String teamId;
        Team team;

        sb.getTeams().forEach((t) -> {
            if(t.getEntries().isEmpty())
                t.unregister();
        });
        while(players.hasNext()) {
            target = players.next();
            User user = getUser(target);
            teamId = (Chat.maxWeight - (getPrimaryGroup(target).getWeight().isPresent() ? getPrimaryGroup(target).getWeight().getAsInt() : 0)) + user.getIdentifier().getName();
            team = sb.getTeam(teamId);
            if(team == null) {
                team = sb.registerNewTeam(teamId);
            }

            team.setPrefix(getChatPrefix(target) + " §8| ");
            team.setColor(ChatColor.GRAY);
            team.addEntry(target.getName());
        }

        players = Bukkit.getOnlinePlayers().iterator();

        while(players.hasNext()) {
            target = players.next();
            if (target != player) {
                sb = target.getScoreboard();
                User user = getUser(player);
                teamId = (Chat.maxWeight - (getPrimaryGroup(player).getWeight().isPresent() ? getPrimaryGroup(player).getWeight().getAsInt() : 0)) + user.getIdentifier().getName();
                team = sb.getTeam(teamId);
                if(team == null) {
                    team = sb.registerNewTeam(teamId);
                }

                team.setPrefix(getChatPrefix(player) + " §8| ");
                team.setColor(ChatColor.GRAY);
                team.addEntry(player.getName());
            }
        }
    }
}
