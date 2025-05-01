package me.drex.vanillapermissions.mixin.bypass.spawn_protection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin {

    @ModifyExpressionValue(
        method = "isUnderSpawnProtection",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/dedicated/DedicatedPlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
        )
    )
    public boolean addSpawnProtectionPermission(boolean original, ServerLevel level, BlockPos pos, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return Permissions.check(serverPlayer, Permission.BYPASS_SPAWN_PROTECTION, original);
        }
        return original;
    }

}
