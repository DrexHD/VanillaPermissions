package me.drex.vanillapermissions.mixin;

import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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
    public int sendOpLevelTwoOrHigher(int permissionLevel) {
        return Math.max(2, permissionLevel);
    }

}
