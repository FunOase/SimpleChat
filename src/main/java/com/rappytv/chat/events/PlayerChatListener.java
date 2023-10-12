package com.rappytv.chat.events;

import com.rappytv.chat.ChatPlugin;
import com.rappytv.chat.commands.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ConstantConditions")
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
            if(!plugin.getConfig().contains("emojis")) {
                plugin.getLogger().severe("Emoji list has to be set!");
            } else {
                for(String emoji : plugin.getConfig().getStringList("emojis")) {
                    try {
                        String[] emojis = emoji.split(";");
                        message = message.replaceAll(emojis[0], emojis[1]);
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                }
                event.setMessage(message);
            }
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

            if(!plugin.getConfig().contains("format.chat.teamChat")) {
                player.sendMessage(ChatPlugin.prefix + "§cEin Fehler ist aufgetreten! Bitte schau in die Logs.");
                plugin.getLogger().severe("Team chat format has to be set!");
                return;
            }

            for(Player all : Bukkit.getOnlinePlayers()) {
                if(all.hasPermission("chat.team"))
                    all.sendMessage(ChatColor.translateAlternateColorCodes(
                            '&',
                            plugin.getConfig().getString("format.chat.teamChat")
                                    .replaceAll("<player>", player.getName())
                                    .replaceAll("<message>", teamMessage)
                    ));
            }
            return;
        }

        if(!plugin.getConfig().contains("format.chat.message") || !plugin.getConfig().contains("format.chat.margin")) {
            player.sendMessage(ChatPlugin.prefix + "§cEin Fehler ist aufgetreten! Bitte schau in die Logs.");
            plugin.getLogger().severe("Chat format and margin format has to be set!");
            return;
        }

        boolean margin = player.hasPermission("chat.format.margin");
        String marginText = plugin.getConfig().getString("format.chat.margin");
        event.setFormat(ChatColor.translateAlternateColorCodes(
                '&',
                plugin.getConfig().getString("format.chat.teamChat")
                        .replaceAll("<prefix>", plugin.getLuckPermsUtil().getPrefix(player))
                        .replaceAll("<suffix>", plugin.getLuckPermsUtil().getSuffix(player))
                        .replace("<margin>", margin ? marginText + "\n" : "")
                        .replace("<margin>", margin ? "\n" + marginText : "")
        ));
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
