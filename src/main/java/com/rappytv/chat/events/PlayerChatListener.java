package com.rappytv.chat.events;

import com.rappytv.chat.Chat;
import com.rappytv.chat.commands.ChatCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerChatListener implements Listener {

    private final Chat plugin;
    private static final Pattern hex = Pattern.compile("#[a-fA-F0-9]{6}");
    private static final Pattern color = Pattern.compile("(?i)&([0-9A-FR])");
    private static final Pattern magic = Pattern.compile("(?i)&([K])");
    private static final Pattern bold = Pattern.compile("(?i)&([L])");
    private static final Pattern strikethrough = Pattern.compile("(?i)&([M])");
    private static final Pattern underline = Pattern.compile("(?i)&([N])");
    private static final Pattern italic = Pattern.compile("(?i)&([O])");

    public PlayerChatListener(Chat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if(!ChatCommand.isEnabled() && !player.hasPermission("chat.manage.bypass")) {
            player.sendMessage(Chat.prefix + "Der Chat ist §bdeaktiviert!");
            e.setCancelled(true);
            return;
        }
        String message = e.getMessage();
        if(player.hasPermission("chat.emojis")) {
            for(String emoji : plugin.getConfig().getStringList("emojis")) {
                String[] emojis = emoji.split(";");
                if(emojis[0] == null || emojis[1] == null) continue;
                message = message.replace(emojis[0], emojis[1]);
            }
            e.setMessage(message);
        }
        if((message.toLowerCase().startsWith("@team") || message.toLowerCase().startsWith("@t")) && player.hasPermission("chat.team")) {
            StringBuilder msg = new StringBuilder();
            String[] words = message.split(" ");
            for(int i = 1; i < words.length; i++) {
                msg.append(words[i]).append(" ");
            }

            for(Player all : Bukkit.getOnlinePlayers()) {
                if(all.hasPermission("chat.team"))
                    all.sendMessage("§8»\n§c§l@TEAM §8| §b" + player.getName() + " §8» §f" + ChatColor.translateAlternateColorCodes('&', msg.toString()) + "\n§8»");
            }
            e.setCancelled(true);
            return;
        }

        String prefix = plugin.getLuckPermsUtil().getChatPrefix(player);

        if(player.hasPermission("chat.format.margin")) {
            e.setFormat("§8»\n§7" + prefix + " §8| §7" + player.getName() + " §8» §7" + translateColorCodes(player, e.getMessage()) + "\n§8»");
        } else {
            e.setFormat(prefix + " §8| §7" + player.getName() + " §8» §7" + translateColorCodes(player, e.getMessage()));
        }
    }

    public static String translateColorCodes(Player player, String message) {
        if(player.hasPermission("chat.format.colors.hex")) {
            Matcher match = hex.matcher(message);
            while(match.find()) {
                String color = message.substring(match.start(), match.end());
                message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                match = hex.matcher(message);
            }
        }
        if(player.hasPermission("chat.format.colors")) {
            message = color.matcher(message).replaceAll("§$1");
        }
        if(player.hasPermission("chat.format.bold")) {
            message = bold.matcher(message).replaceAll("§$1");
        }
        if(player.hasPermission("chat.format.italic")) {
            message = italic.matcher(message).replaceAll("§$1");
        }
        if(player.hasPermission("chat.format.underline")) {
            message = underline.matcher(message).replaceAll("§$1");
        }
        if(player.hasPermission("chat.format.strikethrough")) {
            message = strikethrough.matcher(message).replaceAll("§$1");
        }
        if(player.hasPermission("chat.format.magic")) {
            message = magic.matcher(message).replaceAll("§$1");
        }
        return message;
    }
}
