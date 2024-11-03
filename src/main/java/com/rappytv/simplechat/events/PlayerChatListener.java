package com.rappytv.simplechat.events;

import com.rappytv.simplechat.SimpleChat;
import com.rappytv.simplechat.commands.Chat;
import com.rappytv.rylib.util.Colors;
import com.rappytv.rylib.util.I18n;
import com.rappytv.rylib.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@SuppressWarnings("ConstantConditions")
public class PlayerChatListener implements Listener {

    private final SimpleChat plugin;

    public PlayerChatListener(SimpleChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if(!Chat.isEnabled() && !player.hasPermission("simplechat.manage.bypass")) {
            player.sendMessage(plugin.i18n().translate("listener.chatOff"));
            event.setCancelled(true);
            return;
        }
        if(player.hasPermission("simplechat.emojis")) {
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

        String[] words = message.split(" ");
        for(String sectionName : plugin.getConfig().getConfigurationSection("chats").getKeys(false)) {
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("chats." + sectionName);
            String permission = section.getString("permission");
            boolean hasPermission = Permissions.hasExactPermission(
                    player,
                    "simplechat.chat.*"
            ) || Permissions.hasExactPermission(
                    player,
                    permission
            );
            if(!hasPermission || !section.getStringList("triggers").contains(words[0])) continue;
            event.setCancelled(true);
            if(words.length < 2) {
                player.sendMessage(plugin.i18n().translate("listener.enterText"));
                return;
            }
            String chatMessage = message.substring(words[0].length() + 1);
            for(Player target : Bukkit.getOnlinePlayers()) {
                if(Permissions.hasExactPermission(target, "simplechat.chat.*") || Permissions.hasExactPermission(target, permission))
                    target.sendMessage(Colors.translateCodes(SimpleChat.setPlaceholders(
                            player,
                            plugin.i18n().translate(
                                    "chat.chats",
                                    new I18n.Argument("name", section.getString("name")),
                                    new I18n.Argument("player", player.getName()),
                                    new I18n.Argument("message", chatMessage)
                            )
                    )));
            }
            return;
        }

        boolean margin = player.hasPermission("simplechat.format.margin");
        String marginText = plugin.i18n().translate("chat.margin");
        String prefix = plugin.getLuckPermsUtil().getPrefix(player);
        String suffix = plugin.getLuckPermsUtil().getSuffix(player);
        event.setFormat(Colors.translateCodes(SimpleChat.setPlaceholders(
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
