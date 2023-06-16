package de.salzfrei.objects.cache;

import com.mojang.brigadier.tree.CommandNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class CommandMemory {
    public static HashMap<UUID, Collection<? extends CommandNode<?>>> MEMORY = new HashMap<>();
}
