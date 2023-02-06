package me.drex.vanillapermissions.mc118.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.drex.vanillapermissions.event.ModifyExecuteCommand;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ExecuteCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = "<1.19.1"
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
    private static void fabricpermissions_addPermissionConditionArgument(
            CommandNode<CommandSourceStack> node,
            LiteralArgumentBuilder<CommandSourceStack> argumentBuilder,
            boolean positive,
            CallbackInfoReturnable<ArgumentBuilder<CommandSourceStack, ?>> cir
    ) {
        ModifyExecuteCommand.ADD_CONDITIONAL.invoker().addConditionals(node, argumentBuilder, positive);
    }

    @WrapOperation(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
                    remap = false,
                    ordinal = 0
            )
    )
    private static ArgumentBuilder<CommandSourceStack, ?> vanillaPermissions_addModifierArguments(LiteralArgumentBuilder<CommandSourceStack> instance, ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, Operation<ArgumentBuilder<CommandSourceStack, ?>> original, @Local LiteralCommandNode<CommandSourceStack> root) {
        ModifyExecuteCommand.ADD_MODIFIER.invoker().addModifiers(instance, root);
        return original.call(instance, argumentBuilder);
    }

}
