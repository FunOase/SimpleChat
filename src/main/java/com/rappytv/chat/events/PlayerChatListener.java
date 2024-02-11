package com.rappytv.chat.events;

import com.rappytv.chat.ChatPlugin;
import com.rappytv.chat.commands.Chat;
import com.rappytv.rylib.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@SuppressWarnings("ConstantConditions")
public class PlayerChatListener implements Listener {

    private final ChatPlugin plugin;

    public PlayerChatListener(ChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(!Chat.isEnabled() && !player.hasPermission("chat.manage.bypass")) {
            player.sendMessage(plugin.i18n().translate("listener.chatOff"));
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
        event.setMessage(Colors.translatePlayerCodes(player, event.getMessage(), "chat.format"));
        if((event.getMessage().toLowerCase().startsWith("@team") || event.getMessage().toLowerCase().startsWith("@t")) && player.hasPermission("chat.team")) {
            event.setCancelled(true);
            String msg = event.getMessage();
            String[] words = msg.split(" ");
            if(words.length < 2) {
                player.sendMessage(plugin.i18n().translate("listener.enterText"));
                return;
            }
            String teamMessage = msg.substring(words[0].length() + 1);

            for(Player all : Bukkit.getOnlinePlayers()) {
                if(all.hasPermission("chat.team"))
                    all.sendMessage(Colors.translateCodes(
                            plugin.i18n().translate("chat.teamChat")
                                    .replaceAll("<player>", player.getName())
                                    .replaceAll("<message>", teamMessage)
                    ));
            }
            return;
        }

        boolean margin = player.hasPermission("chat.format.margin");
        String marginText = plugin.i18n().translate("chat.margin");
        String prefix = plugin.getLuckPermsUtil().getPrefix(player);
        String suffix = plugin.getLuckPermsUtil().getSuffix(player);
        event.setFormat(Colors.translateCodes(
                plugin.i18n().translate("chat.message")
                        .replace(
                                "<prefixFormat>",
                                !prefix.isEmpty() ? plugin.i18n().translate("chat.prefixFormat") : ""
                        )
                        .replace(
                                "<suffixFormat>",
                                !suffix.isEmpty() ? plugin.i18n().translate("chat.suffixFormat") : ""
                        )
                        .replace("<playerPrefix>", prefix)
                        .replace("<playerSuffix>", suffix)
                        .replace("<margin1>", margin ? marginText + "\n" : "")
                        .replace("<margin2>", margin ? "\n" + marginText : "")
        ));
    }
}
