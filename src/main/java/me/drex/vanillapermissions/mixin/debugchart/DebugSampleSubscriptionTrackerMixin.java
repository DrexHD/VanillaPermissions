package me.drex.vanillapermissions.mixin.debugchart;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.debugchart.DebugSampleSubscriptionTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugSampleSubscriptionTracker.class)
public class DebugSampleSubscriptionTrackerMixin {

    @ModifyExpressionValue(
        method = "subscribe",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
        )
    )
    public boolean vanillaPermissions_addDebugChartPermission(boolean original, ServerPlayer player) {
        return Permissions.check(player, Permission.DEBUG_CHART, original);
    }

    @ModifyExpressionValue(
        method = "method_56653",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
        )
    )
    public boolean vanillaPermissions_addDebugChartPermission2(boolean original, ServerPlayer player) {
        return Permissions.check(player, Permission.DEBUG_CHART, original);
    }

}
