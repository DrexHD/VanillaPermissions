package me.drex.vanillapermissions.mixin.operator_blocks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {CommandBlock.class, JigsawBlock.class})
public abstract class OperatorBlockMixin extends Block {

    public OperatorBlockMixin(Properties properties) {
        super(properties);
    }

    @ModifyExpressionValue(
            method = "useWithoutItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;canUseGameMasterBlocks()Z"
            )
    )
    public boolean vanillaPermissions_addOperatorBlockEditPermission(boolean original, BlockState state, Level level, BlockPos pos, Player player) {
        return Permissions.check(player, Permission.OPERATOR_BLOCK_VIEW.formatted(BuiltInRegistries.BLOCK.getKey(this).getPath()), original);
    }

}
