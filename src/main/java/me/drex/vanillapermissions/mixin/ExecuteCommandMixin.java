package me.drex.vanillapermissions.mixin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import me.drex.vanillapermissions.event.AddConditionalCallback;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.commands.ExecuteCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExecuteCommand.class)
public abstract class ExecuteCommandMixin {

    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void vanillaPermissions_registerConditionalCallback(CallbackInfo ci) {
        AddConditionalCallback.EVENT.register((node, argumentBuilder, positive) -> {
            argumentBuilder.then(
                    Commands.literal("permission")
                            .then(
                                    Commands.argument("entity", EntityArgument.entity())
                                            .then(
                                                    addConditional(
                                                            node,
                                                            Commands.argument("permission", StringArgumentType.word()),
                                                            positive,
                                                            context -> Permissions.check(EntityArgument.getEntity(context, "entity"), StringArgumentType.getString(context, "permission"))
                                                    )
                                            )
                            )
            );
        });
    }

    @Shadow
    private static ArgumentBuilder<CommandSourceStack, ?> addConditional(CommandNode<CommandSourceStack> root, ArgumentBuilder<CommandSourceStack, ?> builder, boolean positive, ExecuteCommand.CommandPredicate condition) {
        return null;
    }


}
