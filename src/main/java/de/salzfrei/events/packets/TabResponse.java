package de.salzfrei.events.packets;

import com.mojang.brigadier.suggestion.Suggestions;
import de.salzfrei.Main;
import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import de.salzfrei.objects.User;
import net.md_5.bungee.protocol.packet.TabCompleteResponse;
import de.salzfrei.objects.cache.TabMemory;

public class TabResponse extends Protocolize<TabCompleteResponse> {


    public TabResponse() {
        super(TabCompleteResponse.class, Direction.UPSTREAM, 0);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<TabCompleteResponse> packetReceiveEvent) {}

    @Override
    public void packetSend(PacketSendEvent<TabCompleteResponse> e) {
        TabCompleteResponse res = e.packet();
        TabMemory mem = TabMemory.MEMORY.get(e.player().uniqueId());
        if (mem != null) {
            User u = new User(e.player().handle());
            if (res.getSuggestions() != null) {
                Suggestions s = res.getSuggestions();
                if (res.getTransactionId() == mem.transactionId && mem.request.length() >= s.getRange().getStart()) {
                    String start = mem.request.substring(0, s.getRange().getStart());
                    s.getList().removeIf(suggestion -> !Main.getType().isAllowed(u, start + suggestion.getText()));

                    if (!s.getList().isEmpty()) {
                        return;
                    }
                }
            } else {
                res.getCommands().removeIf(s ->!Main.getType().isAllowed(u, s));

                if (!res.getCommands().isEmpty()) {
                    return;
                }
            }
        }
        e.cancelled(true);
    }

}
