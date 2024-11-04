package com.rappytv.simplechat;

import com.rappytv.simplechat.commands.ChatCommand;
import com.rappytv.simplechat.commands.ChatClearCommand;
import com.rappytv.simplechat.commands.EmojiCommand;
import com.rappytv.simplechat.listeners.JoinListener;
import com.rappytv.simplechat.listeners.PlayerChatListener;
import com.rappytv.simplechat.listeners.luckperms.UpdateListener;
import com.rappytv.simplechat.scoreboard.SidebarScoreboard;
import com.rappytv.simplechat.scoreboard.TablistScoreboard;
import com.rappytv.simplechat.util.LuckPermsUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.funoase.sahara.bukkit.Sahara;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleChat extends JavaPlugin {

    public LuckPerms lp;
    private LuckPermsUtil luckPermsUtil;
    private static boolean usingPlaceholderApi = false;

    @Override
    public void onEnable() {
        registerAll();
        saveDefaultConfig();
        Sahara.get().getI18nManager().saveTranslations(this);

        Plugin placeholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if(placeholderAPI != null && placeholderAPI.isEnabled()) {
            getLogger().info("Using PlaceHolderAPI");
            usingPlaceholderApi = true;
        }

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

        new ChatCommand("chat", this).register();
        new ChatClearCommand("chatclear", this).register();
        new EmojiCommand("emoji", this).register();
    }

    public static String setPlaceholders(OfflinePlayer player, String text) {
        if(!usingPlaceholderApi) return text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public LuckPermsUtil getLuckPermsUtil() {
        return luckPermsUtil;
    }
}
