package me.drex.vanillapermissions.mc119.mixin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import me.drex.vanillapermissions.event.AddConditionalCallback;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ExecuteCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.19.1"
        )
)
@Mixin(ExecuteCommand.class)
public abstract class ExecuteCommandMixin {

    @Inject(
            method = "addConditionals",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
                    remap = false,
                    ordinal = 0
            )
    )
    private static void vanillaPermissions_addPermissionConditionArgument(
            CommandNode<CommandSourceStack> node,
            LiteralArgumentBuilder<CommandSourceStack> argumentBuilder,
            boolean positive,
            CommandBuildContext context,
            CallbackInfoReturnable<ArgumentBuilder<CommandSourceStack, ?>> cir
    ) {
        AddConditionalCallback.EVENT.invoker().addConditional(node, argumentBuilder, positive);
    }

}
