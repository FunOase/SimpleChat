package com.rappytv.chat.util;

import com.rappytv.chat.Chat;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@SuppressWarnings({"ConstantConditions", "deprecation"})
public class PlayerConverter {

    private static User getUser(Player player) {
        return Chat.lp.getPlayerAdapter(Player.class).getUser(player);
    }

    private static Group getPrimaryGroup(Player player) {
        User user = getUser(player);
        return Chat.lp.getGroupManager().getGroup(user.getPrimaryGroup());
    }

    public static String getChatPrefix(Player player) {
        Group group = getPrimaryGroup(player);
        if(group.getDisplayName() == null) return null;

        return ChatColor.translateAlternateColorCodes('&', group.getDisplayName());
    }

    public static void setTabPrefix(Player player) {
        Scoreboard sb = player.getScoreboard();
        sb.getTeams().forEach((team) -> {
            if(team.getEntries().isEmpty())
                team.unregister();
        });
        User user = getUser(player);
        String teamId = (Chat.maxWeight - (getPrimaryGroup(player).getWeight().isPresent() ? getPrimaryGroup(player).getWeight().getAsInt() : 0)) + "_" + user.getIdentifier().getName();
        Team team = sb.getTeam(teamId);
        if(team == null) {
            team = sb.registerNewTeam(teamId);
        }

        team.setPrefix(getChatPrefix(player) + " §8| ");
        team.setColor(ChatColor.GRAY);
        team.addPlayer(player);
    }
}
