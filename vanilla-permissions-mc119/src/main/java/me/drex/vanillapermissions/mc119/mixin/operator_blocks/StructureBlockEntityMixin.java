package me.drex.vanillapermissions.mc119.mixin.operator_blocks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.Constants;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static me.drex.vanillapermissions.Constants.block;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)
@Mixin(StructureBlockEntity.class)
public abstract class StructureBlockEntityMixin {

    @ModifyExpressionValue(
            method = "usedBy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;canUseGameMasterBlocks()Z"
            )
    )
    public boolean vanillaPermissions_addCommandBlockEditPermission(boolean original, Player player) {
        return Permissions.check(player, Constants.OPERATOR_BLOCK_VIEW.formatted(block(Blocks.STRUCTURE_BLOCK)), original);
    }

}
