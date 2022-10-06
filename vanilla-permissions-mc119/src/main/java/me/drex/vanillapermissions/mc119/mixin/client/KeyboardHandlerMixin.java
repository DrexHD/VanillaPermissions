package me.drex.vanillapermissions.mc119.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.mc119.VanillaPermissionsMod1_19;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import static me.drex.vanillapermissions.mc119.VanillaPermissionsMod1_19.hasCommandPermission;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {

    @ModifyExpressionValue(
            method = "handleDebugKeys",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;hasPermissions(I)Z"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/KeyboardHandler;copyRecreateCommand(ZZ)V"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/player/LocalPlayer;commandUnsigned(Ljava/lang/String;)Z",
                            ordinal = 0
                    )
            )
    )
    public boolean allowGameModeSwitching(boolean original) {
        return original || VanillaPermissionsMod1_19.hasCommandPermission("gamemode");
    }


}
