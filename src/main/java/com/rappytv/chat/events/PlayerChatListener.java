package com.rappytv.chat.events;

import com.rappytv.chat.ChatPlugin;
import com.rappytv.chat.commands.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerChatListener implements Listener {

    private final ChatPlugin plugin;
    private static final Pattern hex = Pattern.compile("#[a-fA-F0-9]{6}");
    private static final Pattern color = Pattern.compile("(?i)&([0-9A-FR])");
    private static final Pattern magic = Pattern.compile("(?i)&([K])");
    private static final Pattern bold = Pattern.compile("(?i)&([L])");
    private static final Pattern strikethrough = Pattern.compile("(?i)&([M])");
    private static final Pattern underline = Pattern.compile("(?i)&([N])");
    private static final Pattern italic = Pattern.compile("(?i)&([O])");

    public PlayerChatListener(ChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(!Chat.isEnabled() && !player.hasPermission("chat.manage.bypass")) {
            player.sendMessage(ChatPlugin.prefix + "Der Chat ist §bdeaktiviert!");
            event.setCancelled(true);
            return;
        }
        if(player.hasPermission("chat.emojis")) {
            String message = event.getMessage();
            for(String emoji : plugin.getConfig().getStringList("emojis")) {
                String[] emojis = emoji.split(";");
                if(emojis[0] == null || emojis[1] == null) continue;
                message = message.replace(emojis[0], emojis[1]);
            }
            event.setMessage(message);
        }
        event.setMessage(translateColorCodes(player, event.getMessage()));
        if((event.getMessage().toLowerCase().startsWith("@team") || event.getMessage().toLowerCase().startsWith("@t")) && player.hasPermission("chat.team")) {
            event.setCancelled(true);
            String msg = event.getMessage();
            String[] words = msg.split(" ");
            if(words.length < 2) {
                player.sendMessage(ChatPlugin.prefix + "§cBitte gib einen Text an!");
                return;
            }
            String teamMessage = msg.substring(words[0].length() + 1);

            for(Player all : Bukkit.getOnlinePlayers()) {
                if(all.hasPermission("chat.team"))
                    all.sendMessage("§8»\n§c§l@TEAM §8| §b" + player.getName() + " §8» §f" + teamMessage + "\n§8»");
            }
            return;
        }

        String prefix = plugin.getLuckPermsUtil().getChatPrefix(player);

        if(player.hasPermission("chat.format.margin")) {
            event.setFormat("§8»\n§7" + prefix + " §8| §7%s §8» §7%s\n§8»");
        } else {
            event.setFormat(prefix + " §8| §7%s §8» §7%s");
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
