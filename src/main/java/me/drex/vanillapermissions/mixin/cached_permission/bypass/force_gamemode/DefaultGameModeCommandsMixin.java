package me.drex.vanillapermissions.mixin.cached_permission.bypass.force_gamemode;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.commands.DefaultGameModeCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DefaultGameModeCommands.class)
public abstract class DefaultGameModeCommandsMixin {

    @WrapOperation(
        method = "setMode",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;setGameMode(Lnet/minecraft/world/level/GameType;)Z"
        )
    )
    private static boolean addDefaultGameModeOverridePermission(ServerPlayer player, GameType gameType, Operation<Boolean> original) {
        if (Permissions.check(player, Permission.BYPASS_FORCE_GAMEMODE)) {
            return false;
        }
        return original.call(player, gameType);
    }

}
