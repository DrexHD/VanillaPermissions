package me.drex.vanillapermissions.mc116.mixin.debugstick;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.Constants;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = "<1.17-"
        )
)
@Mixin(DebugStickItem.class)
public abstract class DebugStickItemMixin {

    /**
     * In >=1.17 {@link DebugStickItem#handleInteraction(Player, BlockState, LevelAccessor, BlockPos, boolean, ItemStack)} changed signature (return type: void -> boolean)
     */
    @ModifyExpressionValue(
            method = "handleInteraction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;canUseGameMasterBlocks()Z"
            )
    )
    public boolean fabricpermissions_addDebugStickUsePermission(boolean original, Player player, BlockState state) {
        ResourceLocation identifier = Registry.BLOCK.getKey(state.getBlock());
        return Permissions.check(player, Constants.DEBUG_STICK_USE.formatted(Constants.item(Items.DEBUG_STICK), identifier.getNamespace(), identifier.getPath()), original);
    }

}
