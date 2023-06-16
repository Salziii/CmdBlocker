package de.salzfrei.objects;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class User {

    public final ProxiedPlayer player;

    public User(ProxiedPlayer player) {
        this.player = player;
    }

    public ProxiedPlayer getPlayer() {
        return this.player;
    }

    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    public Object getKey() {
        return player.getUniqueId();
    }

    public Object getContext() {
        return player.getServer();
    }

}
