package me.drex.vanillapermissions.mixin.bypass.hunger;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixin {

    @ModifyExpressionValue(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getDifficulty()Lnet/minecraft/world/Difficulty;")
    )
    private Difficulty vanillaPermissions_addBypassHungerPermission(Difficulty difficulty) {
        return Permissions.check((Player)(Object)this, Permission.BYPASS_HUNGER)
                ? Difficulty.PEACEFUL
                : difficulty;
    }

}
