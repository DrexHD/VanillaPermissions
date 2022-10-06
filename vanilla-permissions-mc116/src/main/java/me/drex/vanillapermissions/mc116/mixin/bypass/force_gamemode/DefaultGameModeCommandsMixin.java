package me.drex.vanillapermissions.mc116.mixin.bypass.force_gamemode;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanillapermissions.Constants;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.commands.DefaultGameModeCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = "<1.17-"
        )
)
@Mixin(DefaultGameModeCommands.class)
public abstract class DefaultGameModeCommandsMixin {

    @WrapOperation(
            method = "setMode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;setGameMode(Lnet/minecraft/world/level/GameType;)V"
            )
    )
    private static void fabricpermissions_addDefaultGameModeOverridePermission(ServerPlayer player, GameType gameType, Operation<Void> original) {
        if (!Permissions.check(player, Constants.BYPASS_FORCE_GAMEMODE)) {
            original.call(player, gameType);
        }
    }

}
