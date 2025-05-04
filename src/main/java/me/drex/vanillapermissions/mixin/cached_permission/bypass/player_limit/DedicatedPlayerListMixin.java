package me.drex.vanillapermissions.mixin.cached_permission.bypass.player_limit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import me.drex.vanillapermissions.util.JoinCache;
import me.drex.vanillapermissions.util.Permission;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DedicatedPlayerList.class)
public abstract class DedicatedPlayerListMixin {

    @ModifyExpressionValue(
        method = "canBypassPlayerLimit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/ServerOpList;canBypassPlayerLimit(Lcom/mojang/authlib/GameProfile;)Z"
        )
    )
    public boolean addBypassPlayerLimitPermission(boolean original, GameProfile gameProfile) {
        return JoinCache.getCachedPermissions(gameProfile.getId(), Permission.BYPASS_PLAYER_LIMIT).orElse(original);
    }

}
