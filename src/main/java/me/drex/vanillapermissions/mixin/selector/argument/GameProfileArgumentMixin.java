package me.drex.vanillapermissions.mixin.selector.argument;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

import static me.drex.vanillapermissions.util.ArgumentPermission.validate;

@Mixin(GameProfileArgument.class)
public abstract class GameProfileArgumentMixin {

    @Inject(
        method = "getGameProfiles",
        at = @At("RETURN")
    )
    private static void addGameProfilesPermission(CommandContext<CommandSourceStack> commandContext, String string, CallbackInfoReturnable<Collection<GameProfile>> cir) throws CommandSyntaxException {
        validate(commandContext, string, cir.getReturnValue());
    }
}
