package me.drex.vanillapermissions.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {

    //? if >= 1.21.11 {
    @ModifyExpressionValue(
        method = "handleDebugKeys",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/permissions/PermissionCheck;check(Lnet/minecraft/server/permissions/PermissionSet;)Z"
        )
    )
    public boolean allowDebugKeys(boolean original) {
        return true;
    }
    //? } else {
    /*@ModifyExpressionValue(
        method = "handleDebugKeys",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;hasPermissions(I)Z",
            ordinal = 1
        )
    )
    public boolean allowSpectatorSwitching(boolean original) {
        return true;
    }

    @ModifyExpressionValue(
        method = "handleDebugKeys",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;hasPermissions(I)Z",
            ordinal = 2
        )
    )
    public boolean allowGameModeSwitcher(boolean original) {
        return true;
    }
    *///? }

}
