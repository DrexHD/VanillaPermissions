package me.drex.vanillapermissions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//? if > 1.21.10 {
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
//? }
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    //? if > 1.21.10 {
    @WrapOperation(
        method = "sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/permissions/LevelBasedPermissionSet;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/permissions/LevelBasedPermissionSet;level()Lnet/minecraft/server/permissions/PermissionLevel;"
        )
    )
    private PermissionLevel sendOpLevelTwoOrHigher(LevelBasedPermissionSet instance, Operation<PermissionLevel> original) {
        PermissionLevel permissionLevel = original.call(instance);
        return PermissionLevel.byId(Math.max(2, permissionLevel.id()));
    }
    //? } else {
    /*@ModifyArg(
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
    *///? }

}
