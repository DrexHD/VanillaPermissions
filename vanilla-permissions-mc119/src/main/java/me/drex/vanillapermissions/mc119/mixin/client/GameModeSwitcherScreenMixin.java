package me.drex.vanillapermissions.mc119.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanillapermissions.mc119.VanillaPermissionsMod1_19;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.16"
        )
)
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
        GameModeSwitcherScreen.GameModeIcon icon = optional.get();
        return original || VanillaPermissionsMod1_19.hasCommandPermission(icon.command);
    }

    @WrapOperation(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;getNext()Ljava/util/Optional;"
            )
    )
    public Optional<GameModeSwitcherScreen.GameModeIcon> skipHiddenIcons(GameModeSwitcherScreen.GameModeIcon instance, Operation<Optional<GameModeSwitcherScreen.GameModeIcon>> original) {
        Optional<GameModeSwitcherScreen.GameModeIcon> optional = original.call(instance);
        for (int i = 1; i < GameModeSwitcherScreen.GameModeIcon.values().length; i++) {
            if (optional.isPresent()) {
                GameModeSwitcherScreen.GameModeIcon icon = optional.get();
                if (VanillaPermissionsMod1_19.hasCommandPermission(icon.command)) {
                    return optional;
                } else {
                    optional = original.call(icon);
                }
            } else {
                return optional;
            }
        }
        return optional;
    }

    @WrapWithCondition(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
            )
    )
    public boolean onlyAddAllowed(List<GameModeSwitcherScreen.GameModeSlot> slots, Object gameModeSlot) {
        GameModeSwitcherScreen.GameModeIcon icon = ((GameModeSlotAccessor) gameModeSlot).getIcon();
        return VanillaPermissionsMod1_19.hasCommandPermission(icon.command);
    }


    @Mixin(GameModeSwitcherScreen.GameModeSlot.class)
    public interface GameModeSlotAccessor {

        @Accessor
        GameModeSwitcherScreen.GameModeIcon getIcon();

    }

}
