package me.drex.vanillapermissions.mc119.mixin;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)
@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @ModifyArg(
            method = "sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;I)V"
            ),
            index = 1
    )
    public int vanillaPermissions_sendOpLevelTwoOrHigher(int permissionLevel) {
        return Math.max(2, permissionLevel);
    }

}
