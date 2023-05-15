package me.drex.vanillapermissions.mixin.bypass.whitelist;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
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
        return original || Permissions.check(gameProfile, Permission.BYPASS_WHITELIST).join();
    }

}
