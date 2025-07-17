package me.drex.vanillapermissions.mixin.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

import static me.drex.vanillapermissions.util.ArgumentPermission.validate;

import static java.util.Collections.singleton;

@Mixin(EntityArgument.class)
public abstract class EntityArgumentMixin {

    @Inject(
        method = {
            "getEntity",
            "getPlayer",
        },
        at = @At("RETURN")
    )
    private static void addEntityPermission(CommandContext<CommandSourceStack> commandContext, String string, CallbackInfoReturnable<Entity> cir) throws CommandSyntaxException {
        validate(commandContext, string, singleton(cir.getReturnValue()));
    }

    @Inject(
        method = {
            "getEntities",
            "getOptionalEntities",
            "getPlayers",
            "getOptionalPlayers",
        },
        at = @At("RETURN")
    )
    private static void addEntitiesPermission(CommandContext<CommandSourceStack> commandContext, String string, CallbackInfoReturnable<Collection<Entity>> cir) throws CommandSyntaxException {
        validate(commandContext, string, cir.getReturnValue());
    }
}
