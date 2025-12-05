package me.drex.vanillapermissions.mixin.cached_permission.bypass.player_limit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.JoinCache;
import me.drex.vanillapermissions.util.Permission;
import net.minecraft.server.dedicated.DedicatedPlayerList;
//? if >= 1.21.9 {
import net.minecraft.server.players.NameAndId;
//?} else {
/*import com.mojang.authlib.GameProfile;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DedicatedPlayerList.class)
public abstract class DedicatedPlayerListMixin {

    @ModifyExpressionValue(
        method = "canBypassPlayerLimit",
        at = @At(
            value = "INVOKE",
            //? if >= 1.21.9 {
            target = "Lnet/minecraft/server/players/ServerOpList;canBypassPlayerLimit(Lnet/minecraft/server/players/NameAndId;)Z"
            //?} else {
            /*target = "Lnet/minecraft/server/players/ServerOpList;canBypassPlayerLimit(Lcom/mojang/authlib/GameProfile;)Z"
            *///?}
        )
    )
    public boolean addBypassPlayerLimitPermission(boolean original, /*? if >= 1.21.9 {*/ NameAndId /*?} else {*/ /*GameProfile *//*?}*/ nameAndId) {
        return JoinCache.getCachedPermissions(nameAndId./*? if >= 1.21.9 {*/ id() /*?} else {*/ /*getId() *//*?}*/, Permission.BYPASS_PLAYER_LIMIT).orElse(original);
    }

}
