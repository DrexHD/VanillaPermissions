package me.drex.vanillapermissions.mixin.debugchart;

//? if >= 1.21.9 {
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.debug.ServerDebugSubscribers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerDebugSubscribers.class)
public class DebugSampleSubscriptionTrackerMixin {
    @ModifyExpressionValue(
        method = "hasRequiredPermissions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;isOp(Lnet/minecraft/server/players/NameAndId;)Z"
        )
    )
    public boolean addDebugChartPermission2(boolean original, @Local(argsOnly = true) ServerPlayer serverPlayer) {
        return Permissions.check(serverPlayer, Permission.DEBUG_CHART, original);
    }
}
//? } else if >= 1.20.5 {
/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.debugchart.DebugSampleSubscriptionTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(DebugSampleSubscriptionTracker.class)
public class DebugSampleSubscriptionTrackerMixin {

    @ModifyExpressionValue(
        method = "subscribe",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
        )
    )
    public boolean addDebugChartPermission(boolean original, ServerPlayer player) {
        return Permissions.check(player, Permission.DEBUG_CHART, original);
    }

    @ModifyExpressionValue(
        method = "method_56653",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
        )
    )
    public boolean addDebugChartPermission2(boolean original, @Local(argsOnly = true) Map.Entry<ServerPlayer, Object> entry) {
        return Permissions.check(entry.getKey(), Permission.DEBUG_CHART, original);
    }

}
*/
//? }