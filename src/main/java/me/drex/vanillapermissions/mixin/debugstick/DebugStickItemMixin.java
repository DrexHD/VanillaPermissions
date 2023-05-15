package me.drex.vanillapermissions.mixin.debugstick;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugStickItem.class)
public abstract class DebugStickItemMixin {

    @ModifyExpressionValue(
            method = "handleInteraction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;canUseGameMasterBlocks()Z"
            )
    )
    public boolean vanillaPermissions_addDebugStickUsePermission(boolean original, Player player, BlockState state) {
        ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return Permissions.check(player, Permission.DEBUG_STICK_USE.formatted(BuiltInRegistries.ITEM.getKey(Items.DEBUG_STICK).getPath(), identifier.getNamespace(), identifier.getPath()), original);
    }

}
