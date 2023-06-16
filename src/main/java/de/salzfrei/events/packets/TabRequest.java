package de.salzfrei.events.packets;

import de.salzfrei.objects.cache.TabMemory;
import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import net.md_5.bungee.protocol.packet.TabCompleteRequest;

public class TabRequest extends Protocolize<TabCompleteRequest> {

    public TabRequest() {
        super(TabCompleteRequest.class, Direction.UPSTREAM, 0);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<TabCompleteRequest> e) {
        TabCompleteRequest req = e.packet();
        TabMemory.MEMORY.put(e.player().uniqueId(), new TabMemory(req.getTransactionId(), req.getCursor()));
    }

    @Override
    public void packetSend(PacketSendEvent<TabCompleteRequest> packetSendEvent) {}
}
