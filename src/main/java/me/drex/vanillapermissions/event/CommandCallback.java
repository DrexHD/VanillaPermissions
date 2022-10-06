package me.drex.vanillapermissions.event;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.commands.CommandSourceStack;

public interface CommandCallback {
    Event<CommandCallback> EVENT = EventFactory.createArrayBacked(CommandCallback.class, (callbacks) -> (dispatcher) -> {
        for (CommandCallback callback : callbacks) {
            callback.register(dispatcher);
        }
    });

    /**
     * Called after all vanilla commands have been registered, before other commands get registered.
     *
     * @param dispatcher the command dispatcher to register commands to.
     */
    void register(CommandDispatcher<CommandSourceStack> dispatcher);
}
