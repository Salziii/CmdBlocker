package de.salzfrei.events;

import de.salzfrei.objects.TabMemory;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        TabMemory.MEMORY.remove(e.getPlayer().getUniqueId());
    }

}
