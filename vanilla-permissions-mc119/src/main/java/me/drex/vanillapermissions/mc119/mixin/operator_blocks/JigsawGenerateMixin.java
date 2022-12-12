package me.drex.vanillapermissions.mc119.mixin.operator_blocks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.drex.vanillapermissions.util.RegistryProvider;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.16"
        )
)
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class JigsawGenerateMixin {

    @Shadow public ServerPlayer player;

    @ModifyExpressionValue(
            method = "handleJigsawGenerate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;canUseGameMasterBlocks()Z"
            )
    )
    public boolean vanillaPermissions_addJigsawBlockEditPermission2(boolean original) {
        return Permissions.check(this.player, Permission.OPERATOR_BLOCK_EDIT.formatted(RegistryProvider.blockKey(Blocks.JIGSAW).getPath()), original);
    }

}
