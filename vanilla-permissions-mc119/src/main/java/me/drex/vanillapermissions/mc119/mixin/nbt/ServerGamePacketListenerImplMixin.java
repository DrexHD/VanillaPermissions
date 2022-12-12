package me.drex.vanillapermissions.mc119.mixin.nbt;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
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

    @Shadow public ServerPlayer player;

    @ModifyExpressionValue(
            method = "handleEntityTagQuery",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;hasPermissions(I)Z"
            )
    )
    public boolean vanillaPermissions_addNbtQueryEntityPermission(boolean original) {
        return Permissions.check(this.player, Permission.NBT_QUERY_ENTITY, original);
    }

    @ModifyExpressionValue(
            method = "handleBlockEntityTagQuery",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;hasPermissions(I)Z"
            )
    )
    public boolean vanillaPermissions_addNbtQueryBlockPermission(boolean original) {
        return Permissions.check(this.player, Permission.NBT_QUERY_BLOCK, original);
    }

}
