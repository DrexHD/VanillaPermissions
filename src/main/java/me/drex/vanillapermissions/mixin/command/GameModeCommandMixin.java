package me.drex.vanillapermissions.mixin.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.server.commands.GameModeCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Collections;

@Mixin(GameModeCommand.class)
public abstract class GameModeCommandMixin {

    @Shadow
    private static int setMode(CommandContext<CommandSourceStack> commandContext, Collection<ServerPlayer> collection, GameType gameType) {
        return 0;
    }

    /**
     * @author Drex
     * @reason In version 22w45a the gamemode command was rewritten to use {@link GameModeArgument} instead of literals!
     * This breaks permissions like minecraft.command.gamemode.survival, because the gamemode arguments can no longer be
     * controlled by the server. This reverts this change!
     */
    @Overwrite
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder = Commands.literal("gamemode")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2));

        for (GameType gameType : GameType.values()) {
            literalArgumentBuilder.then(
                    Commands.literal(gameType.getName())
                            .executes(context -> setMode(context, Collections.singleton(context.getSource().getPlayerOrException()), gameType))
                            .then(
                                    Commands.argument("target", EntityArgument.players())
                                            .executes(commandContext -> setMode(commandContext, EntityArgument.getPlayers(commandContext, "target"), gameType))
                            )
            );
        }
        dispatcher.register(literalArgumentBuilder);
    }


}
