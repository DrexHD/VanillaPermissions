package me.drex.vanillapermissions.mixin.bypass.move_speed;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
            method = "handleMovePlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;isSingleplayerOwner()Z"
            )
    )
    public boolean vanillaPermissions_addBypassMoveSpeedPlayerPermission(boolean original) {
        return Permissions.check(this.player, Permission.BYPASS_MOVE_SPEED_PLAYER, original);
    }

    @ModifyExpressionValue(
            method = "handleMoveVehicle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;isSingleplayerOwner()Z"
            )
    )
    public boolean vanillaPermissions_addBypassMoveSpeedVehiclePermission(boolean original) {
        ResourceLocation identifier = BuiltInRegistries.ENTITY_TYPE.getKey(this.player.getRootVehicle().getType());
        return Permissions.check(this.player, Permission.BYPASS_MOVE_SPEED_VEHICLE.formatted(identifier.getNamespace(), identifier.getPath()), original);
    }
}
