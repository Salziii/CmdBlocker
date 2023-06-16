package de.salzfrei.events;

import de.salzfrei.objects.ProtocolizePacketListener;
import de.salzfrei.objects.TabMemory;
import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import net.md_5.bungee.protocol.packet.TabCompleteRequest;

public class PacketTabRequestListener extends ProtocolizePacketListener<TabCompleteRequest> {

    public PacketTabRequestListener() {
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
