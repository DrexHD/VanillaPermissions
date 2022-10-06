package me.drex.vanillapermissions.event;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

public interface AddConditionalCallback {

    Event<AddConditionalCallback> EVENT = EventFactory.createArrayBacked(AddConditionalCallback.class, (callbacks) -> (node, argumentBuilder, positive) -> {
        for (AddConditionalCallback callback : callbacks) {
            callback.addConditional(node, argumentBuilder, positive);
        }
    });

    /**
     * Called from {@link net.minecraft.server.commands.ExecuteCommand#addConditionals(CommandNode, LiteralArgumentBuilder, boolean, CommandBuildContext)}
     * to register permission conditional
     *
     * @param node
     * @param argumentBuilder parent argument node
     * @param positive whether the conditional is positive or negated
     */
    void addConditional(CommandNode<CommandSourceStack> node, LiteralArgumentBuilder<CommandSourceStack> argumentBuilder, boolean positive);

}
