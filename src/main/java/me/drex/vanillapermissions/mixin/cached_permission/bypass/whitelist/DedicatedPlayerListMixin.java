package me.drex.vanillapermissions.mixin.cached_permission.bypass.whitelist;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import me.drex.vanillapermissions.util.Arguments;
import me.drex.vanillapermissions.util.IConnection;
import me.drex.vanillapermissions.util.Permission;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DedicatedPlayerList.class)
public abstract class DedicatedPlayerListMixin {

    @ModifyReturnValue(
        method = "isWhiteListed",
        at = @At("RETURN")
    )
    public boolean vanillaPermissions_addBypassWhitelistPermission(boolean original, GameProfile gameProfile) {
        if (original) {
            return true;
        }
        if (Arguments.CONNECTION.get() instanceof IConnection connection) {
            return connection.vanillaPermissions$getCachedPermission(Permission.BYPASS_WHITELIST);
        }
        return false;
    }

}
