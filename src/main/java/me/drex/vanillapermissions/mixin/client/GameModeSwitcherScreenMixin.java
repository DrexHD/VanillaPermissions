package me.drex.vanillapermissions.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(GameModeSwitcherScreen.class)
public abstract class GameModeSwitcherScreenMixin {

    @ModifyExpressionValue(
            method = "switchToHoveredGameMode(Lnet/minecraft/client/Minecraft;Ljava/util/Optional;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;hasPermissions(I)Z"
            )
    )
    private static boolean hasCommandAccess(boolean original, Minecraft minecraft, Optional<GameModeSwitcherScreen.GameModeIcon> optional) {
        return true;
    }

}