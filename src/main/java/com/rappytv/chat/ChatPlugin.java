package com.rappytv.chat;

import com.rappytv.chat.commands.Chat;
import com.rappytv.chat.commands.ChatClear;
import com.rappytv.chat.commands.Emoji;
import com.rappytv.chat.events.JoinListener;
import com.rappytv.chat.events.PlayerChatListener;
import com.rappytv.chat.events.luckperms.UpdateListener;
import com.rappytv.chat.scoreboard.SidebarScoreboard;
import com.rappytv.chat.scoreboard.TablistScoreboard;
import com.rappytv.chat.util.LuckPermsUtil;
import com.rappytv.rylib.util.I18n;
import com.rappytv.rylib.util.UpdateChecker;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatPlugin extends JavaPlugin {

    public LuckPerms lp;
    private I18n i18n;
    private LuckPermsUtil luckPermsUtil;

    @Override
    public void onEnable() {
        registerAll();
        saveDefaultConfig();
        i18n = new I18n(this);
        new UpdateChecker<>(this).setArtifactFormat(
                "ci.rappytv.com",
                getName(),
                "com.rappytv"
        );

        Plugin placeholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if(placeholderAPI == null || placeholderAPI.isEnabled())
            getLogger().info("Not using PlaceholderAPI.");

        SidebarScoreboard.init(this);
        TablistScoreboard.init(this);
        LuckPerms provider = Bukkit.getServicesManager().load(LuckPerms.class);
        if (provider != null) {
            lp = provider;
            getLogger().info("Luckperms successfully loaded!");
            new UpdateListener(this);
        }
        luckPermsUtil = new LuckPermsUtil(this);
        Bukkit.getServicesManager().register(LuckPermsUtil.class, luckPermsUtil, this, ServicePriority.Normal);
        for(Player all : Bukkit.getOnlinePlayers()) {
            luckPermsUtil.setTabPrefix(all);
        }
    }

    private void registerAll() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(this), this);
        pm.registerEvents(new PlayerChatListener(this), this);

        new Chat("chat", this);
        new ChatClear("chatclear", this);
        new Emoji("emoji", this);
    }

    public static String setPlaceholders(OfflinePlayer player, String text) {
        try {
            return PlaceholderAPI.setPlaceholders(player, text);
        } catch (Throwable e) {
            return text;
        }
    }

    public I18n i18n() {
        return i18n;
    }

    public LuckPermsUtil getLuckPermsUtil() {
        return luckPermsUtil;
    }
}
