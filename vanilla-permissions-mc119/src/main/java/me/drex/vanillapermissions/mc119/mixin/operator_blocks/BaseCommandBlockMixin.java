package me.drex.vanillapermissions.mc119.mixin.operator_blocks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.drex.vanillapermissions.util.RegistryProvider;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BaseCommandBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)
@Mixin(BaseCommandBlock.class)
public abstract class BaseCommandBlockMixin {

    @ModifyExpressionValue(
            method = "usedBy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;canUseGameMasterBlocks()Z"
            )
    )
    public boolean vanillaPermissions_addCommandBlockMinecartOpenPermission(boolean original, Player player) {
        return Permissions.check(player, Permission.OPERATOR_BLOCK_VIEW.formatted(RegistryProvider.itemKey(Items.COMMAND_BLOCK_MINECART).getPath()), original);
    }

}
