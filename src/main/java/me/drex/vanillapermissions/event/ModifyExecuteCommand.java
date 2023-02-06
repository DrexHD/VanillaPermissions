package me.drex.vanillapermissions.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

public final class ModifyExecuteCommand {

    public static final Event<AddConditionalCallback> ADD_CONDITIONAL = EventFactory.createArrayBacked(AddConditionalCallback.class, (callbacks) -> (node, argumentBuilder, positive) -> {
        for (AddConditionalCallback callback : callbacks) {
            callback.addConditionals(node, argumentBuilder, positive);
        }
    });

    public static final Event<AddModifierCallback> ADD_MODIFIER = EventFactory.createArrayBacked(AddModifierCallback.class, (callbacks) -> (literal, root) -> {
        for (AddModifierCallback callback : callbacks) {
            callback.addModifiers(literal, root);
        }
    });

    @FunctionalInterface
    public interface AddConditionalCallback {
        /**
         * Called from {@link net.minecraft.server.commands.ExecuteCommand#addConditionals(CommandNode, LiteralArgumentBuilder, boolean, CommandBuildContext)}
         * to register permission conditional
         *
         * @param node
         * @param argumentBuilder parent argument node
         * @param positive whether the conditional is positive or negated
         */
        void addConditionals(CommandNode<CommandSourceStack> node, LiteralArgumentBuilder<CommandSourceStack> argumentBuilder, boolean positive);
    }

    @FunctionalInterface
    public interface AddModifierCallback {
        /**
         * Called from {@link net.minecraft.server.commands.ExecuteCommand#register(CommandDispatcher, CommandBuildContext)}
         * to register source modifiers
         *
         * @param literal parent argument node
         * @param root command node, used for {@link com.mojang.brigadier.builder.ArgumentBuilder#fork(CommandNode, RedirectModifier)}
         */
        void addModifiers(LiteralArgumentBuilder<CommandSourceStack> literal, LiteralCommandNode<CommandSourceStack> root);
    }

}
