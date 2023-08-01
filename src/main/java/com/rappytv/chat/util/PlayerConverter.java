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
        System.out.print(group.getDisplayName() + " " + group.getFriendlyName());
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
        System.out.println(teamId);
        Team team = sb.getTeam(teamId);
        if(team == null) {
            team = sb.registerNewTeam(teamId);
        }

        team.setPrefix(getChatPrefix(player) + " §8| ");
        team.setColor(ChatColor.GRAY);
        team.addPlayer(player);

//        Iterator<? extends Player> players = Bukkit.getOnlinePlayers().iterator();
//
//        Player target;
//        Scoreboard sb;
//        String team;
//        while(players.hasNext()) {
//            target = players.next();
//            sb = player.getScoreboard();
//            User user = getUser(target);
//            team = (getPrimaryGroup(target).getWeight().isPresent() ? getPrimaryGroup(target).getWeight().getAsInt() : 0) + user.getIdentifier().getName();
//            if(sb.getTeam(team) == null) {
//                sb.registerNewTeam(team);
//            }
//
//            sb.getTeam(team).setPrefix(getPrefix(target));
//            sb.getTeam(team).color(NamedTextColor.GRAY);
//            sb.getTeam(team).addPlayer(target);
//        }
//
//        players = Bukkit.getOnlinePlayers().iterator();
//
//        while(players.hasNext()) {
//            target = players.next();
//            if (target != player) {
//                sb = target.getScoreboard();
//                User user = getUser(player);
//                team = (getPrimaryGroup(player).getWeight().isPresent() ? getPrimaryGroup(player).getWeight().getAsInt() : 0) + user.getIdentifier().getName();
//                if(sb.getTeam(team) == null) {
//                    sb.registerNewTeam(team);
//                }
//
//                sb.getTeam(team).setPrefix(getPrefix(player));
//                sb.getTeam(team).color(NamedTextColor.GRAY);
//                sb.getTeam(team).addPlayer(player);
//            }
//        }
    }
}
