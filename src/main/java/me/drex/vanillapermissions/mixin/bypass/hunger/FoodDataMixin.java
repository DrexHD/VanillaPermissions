package me.drex.vanillapermissions.mixin.bypass.hunger;

import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @ModifyVariable(
            method = "tick",
            at = @At("LOAD")
    )
    private Difficulty modifyDifficulty(Difficulty difficulty, Player player) {
        return Permissions.check(player, Permission.BYPASS_HUNGER)
                ? Difficulty.PEACEFUL
                : difficulty;
    }
}
