package me.drex.vanillapermissions.mixin.nbt;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Shadow
    public ServerPlayer player;

    @ModifyExpressionValue(
        method = "handleEntityTagQuery",
        at = @At(
            value = "INVOKE",
            //? if > 1.21.10 {
            target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"
            //? } else {
            /*target = "Lnet/minecraft/server/level/ServerPlayer;hasPermissions(I)Z"
            *///? }
        )
    )
    public boolean addNbtQueryEntityPermission(boolean original) {
        return Permissions.check(this.player, Permission.NBT_QUERY_ENTITY, original);
    }

    @ModifyExpressionValue(
        method = "handleBlockEntityTagQuery",
        at = @At(
            value = "INVOKE",
            //? if > 1.21.10 {
            target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"
            //? } else {
            /*target = "Lnet/minecraft/server/level/ServerPlayer;hasPermissions(I)Z"
            *///? }
        )
    )
    public boolean addNbtQueryBlockPermission(boolean original) {
        return Permissions.check(this.player, Permission.NBT_QUERY_BLOCK, original);
    }

}
