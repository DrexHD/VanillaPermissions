package me.drex.vanillapermissions.mc119.mixin.bypass.spawn_protection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)
@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin {

    @ModifyExpressionValue(
            method = "isUnderSpawnProtection",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/dedicated/DedicatedPlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
            )
    )
    public boolean vanillaPermissions_addSpawnProtectionPermission(boolean original, ServerLevel level, BlockPos pos, Player player) {
        return Permissions.check(player, Permission.BYPASS_SPAWN_PROTECTION, original);
    }

}
