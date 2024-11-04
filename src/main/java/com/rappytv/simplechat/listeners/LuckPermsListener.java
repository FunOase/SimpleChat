package com.rappytv.simplechat.listeners;

import com.rappytv.simplechat.SimpleChat;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.EventSubscription;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("rawtypes")
public class LuckPermsListener {

    private final SimpleChat plugin;
    private final Set<EventSubscription> subscriptions = new HashSet<>();

    public LuckPermsListener(SimpleChat plugin) {
        this.plugin = plugin;
        EventBus eventBus = plugin.lp.getEventBus();

        subscriptions.add(eventBus.subscribe(UserPromoteEvent.class, (e) -> this.onUpdateEvent(e.getUser())));
        subscriptions.add(eventBus.subscribe(UserDemoteEvent.class, (e) -> this.onUpdateEvent(e.getUser())));
        subscriptions.add(eventBus.subscribe(UserDataRecalculateEvent.class, (e) -> {
            e.getData().invalidate();
            this.onUpdateEvent(e.getUser());
        }));
    }

    public void close() {
        for (EventSubscription subscription : subscriptions) {
            subscription.close();
        }
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
