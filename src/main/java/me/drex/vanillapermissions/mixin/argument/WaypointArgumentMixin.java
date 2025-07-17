package me.drex.vanillapermissions.mixin.argument;
//? if >= 1.21.6 {
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.WaypointArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.drex.vanillapermissions.util.ArgumentPermission.validate;

import static java.util.Collections.singleton;

@Mixin(WaypointArgument.class)
public abstract class WaypointArgumentMixin {

    @Inject(
        method = "getWaypoint",
        at = @At("RETURN")
    )
    private static void addWaypointPermission(CommandContext<CommandSourceStack> commandContext, String string, CallbackInfoReturnable<WaypointTransmitter> cir, @Local Entity entity) throws CommandSyntaxException {
        validate(commandContext, string, singleton(entity));
    }
}
//?} else {

/*import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftServer.class)
public abstract class WaypointArgumentMixin {

}
*///?}
