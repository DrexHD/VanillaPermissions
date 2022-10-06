package me.drex.vanillapermissions.mc119.mixin.operator_blocks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.Constants;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.GameMasterBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static me.drex.vanillapermissions.Constants.block;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)
@Mixin(GameMasterBlockItem.class)
public abstract class GameMasterBlockItemMixin extends BlockItem {

    public GameMasterBlockItemMixin(Block block, Properties properties) {
        super(block, properties);
    }

    @ModifyExpressionValue(
            method = "getPlacementState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;canUseGameMasterBlocks()Z"
            )
    )
    public boolean vanillaPermissions_addCommandBlockPlacePermission(boolean original, BlockPlaceContext context) {
        assert context.getPlayer() != null;
        return Permissions.check(context.getPlayer(), Constants.OPERATOR_BLOCK_PLACE.formatted(block(getBlock())), original);
    }

}
