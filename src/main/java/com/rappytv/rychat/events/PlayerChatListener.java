package com.rappytv.rychat.events;

import com.rappytv.rychat.RyChat;
import com.rappytv.rychat.commands.Chat;
import com.rappytv.rylib.util.Colors;
import com.rappytv.rylib.util.I18n;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@SuppressWarnings("ConstantConditions")
public class PlayerChatListener implements Listener {

    private final RyChat plugin;

    public PlayerChatListener(RyChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(!Chat.isEnabled() && !player.hasPermission("rychat.manage.bypass")) {
            player.sendMessage(plugin.i18n().translate("listener.chatOff"));
            event.setCancelled(true);
            return;
        }
        if(player.hasPermission("rychat.emojis")) {
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
        if((event.getMessage().toLowerCase().startsWith("@team") || event.getMessage().toLowerCase().startsWith("@t")) && player.hasPermission("rychat.team")) {
            event.setCancelled(true);
            String msg = event.getMessage();
            String[] words = msg.split(" ");
            if(words.length < 2) {
                player.sendMessage(plugin.i18n().translate("listener.enterText"));
                return;
            }
            String teamMessage = msg.substring(words[0].length() + 1);

            for(Player all : Bukkit.getOnlinePlayers()) {
                if(all.hasPermission("rychat.team"))
                    all.sendMessage(Colors.translateCodes(RyChat.setPlaceholders(
                            player,
                            plugin.i18n().translate(
                                    "chat.teamChat",
                                    new I18n.Argument("player", player.getName()),
                                    new I18n.Argument("message", teamMessage)
                            )
                    )));
            }
            return;
        }

        boolean margin = player.hasPermission("rychat.format.margin");
        String marginText = plugin.i18n().translate("chat.margin");
        String prefix = plugin.getLuckPermsUtil().getPrefix(player);
        String suffix = plugin.getLuckPermsUtil().getSuffix(player);
        event.setFormat(Colors.translateCodes(RyChat.setPlaceholders(
                player,
                plugin.i18n().translate(
                        "chat.message",
                        new I18n.Argument(
                                "prefixFormat",
                                !prefix.isEmpty() ? plugin.i18n().translate("chat.prefixFormat") : ""
                        ),
                        new I18n.Argument(
                                "suffixFormat",
                                !suffix.isEmpty() ? plugin.i18n().translate("chat.suffixFormat") : ""
                        ),
                        new I18n.Argument("playerPrefix", prefix),
                        new I18n.Argument("playerSuffix", suffix),
                        new I18n.Argument("margin1", margin ? marginText + "\n" : ""),
                        new I18n.Argument("margin2", margin ? "\n" + marginText : "")
                )
        )));
    }
}
