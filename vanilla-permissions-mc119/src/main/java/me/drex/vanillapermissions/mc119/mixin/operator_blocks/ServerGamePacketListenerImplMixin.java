package me.drex.vanillapermissions.mc119.mixin.operator_blocks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.Constants;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static me.drex.vanillapermissions.Constants.block;
import static me.drex.vanillapermissions.Constants.item;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Shadow public ServerPlayer player;

    @ModifyExpressionValue(
            method = "handleSetCommandBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;canUseGameMasterBlocks()Z"
            )
    )
    public boolean vanillaPermissions_addCommandBlockEditPermission(boolean original) {
        return Permissions.check(this.player, Constants.OPERATOR_BLOCK_EDIT.formatted(block(Blocks.COMMAND_BLOCK)), original);
    }

    @ModifyExpressionValue(
            method = "handleSetJigsawBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;canUseGameMasterBlocks()Z"
            )
    )
    public boolean vanillaPermissions_addJigsawBlockEditPermission(boolean original) {
        return Permissions.check(this.player, Constants.OPERATOR_BLOCK_EDIT.formatted(block(Blocks.JIGSAW)), original);
    }

    @ModifyExpressionValue(
            method = "handleSetStructureBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;canUseGameMasterBlocks()Z"
            )
    )
    public boolean vanillaPermissions_addStructureBlockEditPermission(boolean original) {
        return Permissions.check(this.player, Constants.OPERATOR_BLOCK_EDIT.formatted(block(Blocks.STRUCTURE_BLOCK)), original);
    }

    @ModifyExpressionValue(
            method = "handleSetCommandMinecart",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;canUseGameMasterBlocks()Z"
            )
    )
    public boolean vanillaPermissions_addCommandBlockMinecartEditPermission(boolean original) {
        return Permissions.check(this.player, Constants.OPERATOR_BLOCK_EDIT.formatted(item(Items.COMMAND_BLOCK_MINECART)), original);
    }

}
