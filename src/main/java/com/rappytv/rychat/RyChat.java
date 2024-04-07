package com.rappytv.rychat;

import com.rappytv.rychat.commands.Chat;
import com.rappytv.rychat.commands.ChatClear;
import com.rappytv.rychat.commands.Emoji;
import com.rappytv.rychat.events.JoinListener;
import com.rappytv.rychat.events.PlayerChatListener;
import com.rappytv.rychat.events.luckperms.UpdateListener;
import com.rappytv.rychat.scoreboard.SidebarScoreboard;
import com.rappytv.rychat.scoreboard.TablistScoreboard;
import com.rappytv.rychat.util.LuckPermsUtil;
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

public final class RyChat extends JavaPlugin {

    public LuckPerms lp;
    private I18n i18n;
    private LuckPermsUtil luckPermsUtil;

    @Override
    public void onEnable() {
        registerAll();
        saveDefaultConfig();
        i18n = new I18n(this);
        new UpdateChecker<>(
                this,
                () -> getConfig().getBoolean("checkForUpdates")
        ).setArtifactFormat(
                "ci.rappytv.com",
                getName(),
                "com.rappytv",
                "Minecraft Plugins"
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
