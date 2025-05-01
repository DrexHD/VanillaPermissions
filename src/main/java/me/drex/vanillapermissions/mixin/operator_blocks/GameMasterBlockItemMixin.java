package me.drex.vanillapermissions.mixin.operator_blocks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.GameMasterBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
    public boolean addCommandBlockPlacePermission(boolean original, BlockPlaceContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            return Permissions.check(serverPlayer, Permission.OPERATOR_BLOCK_PLACE.formatted(BuiltInRegistries.BLOCK.getKey(getBlock()).getPath()), original);
        }
        return original;
    }

}
