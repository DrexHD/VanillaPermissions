package me.drex.vanillapermissions.mc119.mixin.bypass.move_speed;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.Constants;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)
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
        return Permissions.check(this.player, Constants.BYPASS_MOVE_SPEED_PLAYER, original);
    }

    @ModifyExpressionValue(
            method = "handleMoveVehicle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;isSingleplayerOwner()Z"
            )
    )
    public boolean vanillaPermissions_addBypassMoveSpeedVehiclePermission(boolean original) {
        ResourceLocation identifier = Registry.ENTITY_TYPE.getKey(this.player.getRootVehicle().getType());
        return Permissions.check(this.player, Constants.BYPASS_MOVE_SPEED_VEHICLE.formatted(identifier.getNamespace(), identifier.getPath()), original);
    }
}
