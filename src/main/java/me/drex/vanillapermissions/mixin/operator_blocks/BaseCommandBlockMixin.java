package me.drex.vanillapermissions.mixin.operator_blocks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BaseCommandBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCommandBlock.class)
public abstract class BaseCommandBlockMixin {

    @ModifyExpressionValue(
        method = "usedBy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;canUseGameMasterBlocks()Z"
        )
    )
    public boolean addCommandBlockMinecartOpenPermission(boolean original, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return Permissions.check(serverPlayer, Permission.OPERATOR_BLOCK_VIEW.formatted(BuiltInRegistries.ITEM.getKey(Items.COMMAND_BLOCK_MINECART).getPath()), original);
        }
        return original;
    }

}
