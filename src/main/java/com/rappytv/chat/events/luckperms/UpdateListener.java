package com.rappytv.chat.events.luckperms;

import com.rappytv.chat.Chat;
import com.rappytv.chat.util.LuckPermsUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UpdateListener {

    private final Chat plugin;

    public UpdateListener(Chat plugin) {
        this.plugin = plugin;
        EventBus eventBus = plugin.lp.getEventBus();

        eventBus.subscribe(UserPromoteEvent.class, (e) -> this.onUpdateEvent(e.getUser()));
        eventBus.subscribe(UserDemoteEvent.class, (e) -> this.onUpdateEvent(e.getUser()));
        eventBus.subscribe(UserDataRecalculateEvent.class, (e) -> this.onUpdateEvent(e.getUser()));
    }

    private void onUpdateEvent(User user) {
        UUID uuid = user.getUniqueId();
        Player player = Bukkit.getPlayer(uuid);

        try {
            if (player != null) {
                plugin.getLuckPermsUtil().setTabPrefix(player);
            }
        } catch (Exception ignored) {
        }
    }
}
