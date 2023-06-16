package de.salzfrei.events;

import de.salzfrei.Main;
import de.salzfrei.objects.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TabComplete implements Listener {

    @EventHandler
    public void onTab(TabCompleteEvent e) {
        if (e.getSender() instanceof ProxiedPlayer) {
            User u = new User((ProxiedPlayer) e.getSender());
            if (!Main.getType().isAllowed(u, e.getCursor())) {
                e.setCancelled(true);
            } else {
                e.getSuggestions().removeIf(s -> !Main.getType().isAllowed(u, e.getCursor() + " " + s));
            }
        }
    }

}
